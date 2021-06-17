package web.task.track.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.task.track.domain.*;
import web.task.track.dto.AddTaskDto;
import web.task.track.exception.ObjectNotFoundException;
import web.task.track.exception.WrongRoleException;
import web.task.track.exception.WrongStatusException;
import web.task.track.exception.WrongUserException;
import web.task.track.exception.constant.WrongUserExceptionConstants;
import web.task.track.repository.TaskRepository;
import web.task.track.service.FeatureService;
import web.task.track.service.RoleService;
import web.task.track.service.TaskService;
import web.task.track.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final FeatureService featureService;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           FeatureService featureService,
                           UserService userService,
                           RoleService roleService) {
        this.taskRepository = taskRepository;
        this.featureService = featureService;
        this.userService = userService;
        this.roleService = roleService;
    }

    /*
    Получении истории смены текущего исполнителя у задания.
    */
    @Override
    public List<Task> getTaskEditHistory(Integer taskID) {
        List<Task> historyList = new ArrayList<>();
        taskRepository.findRevisions(taskID).get().forEach(task -> {
            task.getEntity().setEditVersion(task.getMetadata());
            historyList.add(task.getEntity());
        });
        return historyList;
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task findById(Integer id) throws ObjectNotFoundException {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Task Not Found with id: " + id));
    }

    @Override
    public void save(Task task) {
        taskRepository.save(task);
    }

    @Override
    public void deleteById(Integer id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Set<Task> getTasksInFeature(Integer id) throws ObjectNotFoundException {
        Feature feature = featureService.findById(id);
        return feature.getTasks();
    }


    @Override
    public Task findByUserAndTitleAndStatus(User user, String title, EStatus status) throws ObjectNotFoundException {
        return taskRepository.findByUserAndTitleAndStatus(user, title, status)
                .orElseThrow(() -> new ObjectNotFoundException("Task not found with user: " + user +
                                                                ", title: " + title + ", status: " + status));
    }

    /*
     Создание Task. Она сразу переводится на DEVELOPER, Status переводится в IN_PROGRESS.
     */
    @Override
    public Task add(AddTaskDto addTaskDto, String username) throws ObjectNotFoundException, WrongRoleException {
        User user = userService.findByUsername(username);
        Feature feature = featureService.findById(addTaskDto.getFeatureId());
        if (!feature.getUsers().contains(user))
            throw new ObjectNotFoundException("The user is not involved in the development feature");
        Task task = new Task(addTaskDto.getTitle(), addTaskDto.getDescription(), user, feature, EStatus.OPEN);
        taskRepository.save(task);
        return task;
    }

    @Override
    public Task assignToDeveloper(Integer id, String devUsername,String principalUsername) throws ObjectNotFoundException,
                                                                                                  WrongRoleException,
                                                                                                  WrongStatusException,
                                                                                                  WrongUserException {
        Task task = findById(id);
        if (!task.getUser().equals(userService.findByUsername(principalUsername)))
            throw new WrongUserException(WrongUserExceptionConstants.NOT_THE_CURRENT_ASSIGNEE);
        User nextUser = userService.findByUsername(devUsername);
        Role nextRole = roleService.findByName(ERole.ROLE_DEVELOPER);
        Feature feature = task.getFeature();
        if (!feature.getUsers().contains(nextUser))
            throw new ObjectNotFoundException("The nextUser is not involved in the development feature");
        if (!nextUser.getRoles().contains(nextRole))
            throw new WrongRoleException("This nextUser is not a developer");
        if(task.getStatus().equals(EStatus.OPEN)){
            task.setUser(nextUser);
            task.setStatus(EStatus.IN_PROGRESS);
            save(task);
            return task;
        }
        else throw new WrongStatusException("Task is not in status OPEN");

    }

    /*
    Решение задачи юзером с ролью developer. Статус переводится в RESOLVED.
    */
    @Override
    public void resolveTask(Task task, User user) throws  WrongUserException {
        User currentUser = task.getUser();
        if (!currentUser.equals(user))
            throw new WrongUserException(WrongUserExceptionConstants.NOT_THE_CURRENT_ASSIGNEE);
        task.setStatus(EStatus.RESOLVED);
        taskRepository.save(task);
    }

    @Override
    public Task assignToTester(Integer id, String testerUsername, String principalUsername) throws ObjectNotFoundException,
                                                                                                   WrongUserException,
                                                                                                   WrongRoleException,
                                                                                                   WrongStatusException {
        Task task = findById(id);
        if (!task.getUser().equals(userService.findByUsername(principalUsername)))
            throw new WrongUserException(WrongUserExceptionConstants.NOT_THE_CURRENT_ASSIGNEE);
        User nextUser = userService.findByUsername(testerUsername);
        Role nextRole = roleService.findByName(ERole.ROLE_TESTER);
        Feature feature = task.getFeature();
        if (!feature.getUsers().contains(nextUser))
            throw new ObjectNotFoundException("The nextUser is not involved in the development feature");
        if (!nextUser.getRoles().contains(nextRole))
            throw new WrongRoleException("This nextUser is not a tester");
        if(task.getStatus().equals(EStatus.RESOLVED)){
            task.setUser(nextUser);
            save(task);
            return task;
        }
        else throw new WrongStatusException("Task is not in status RESOLVED");
    }

    /*
    Возврат тестером задания на предыдущего developer. Из аудиторской таблицы выбираются
    записи конкретной bugTask. Затем из этой выборки идет получение записей, в которых
    текущий юзер - developer. Task возвращается последнему developer, который с ней работал.
    Также создается Bug для данной Task и ее статус переводится обратно в IN_PROGRESS.
    */
    @Override
    public void returnTask(Task bugTask, String principalUsername) throws ObjectNotFoundException, WrongUserException, WrongStatusException {

        if (!bugTask.getUser().equals(userService.findByUsername(principalUsername)))
            throw new WrongUserException(WrongUserExceptionConstants.NOT_THE_CURRENT_ASSIGNEE);
        Set<Bug> bugs = bugTask.getBug();
        if (bugs == null)
            throw new ObjectNotFoundException("There must be a bug to return the task");
        boolean isOpenBug = bugs.stream().anyMatch(bug -> bug.getStatus().equals(EStatus.OPEN));
        if(!isOpenBug)
            throw new WrongStatusException("No bugs are in the state OPEN");
        List<Task> historyTasks = getTaskEditHistory(bugTask.getId());
        Role developerRole = roleService.findByName(ERole.ROLE_DEVELOPER);
        List<User> userInTaskEditHistory = userService.findUserInTaskEditHistory(historyTasks);
        List<User> devUsers = userInTaskEditHistory.stream()
                                   .filter(user -> user.getRoles().contains(developerRole)).collect(Collectors.toList());
        User previousUser = devUsers.get(devUsers.size() - 1);
        bugTask.setUser(previousUser);
        bugTask.setStatus(EStatus.IN_PROGRESS);
        save(bugTask);
    }

    /*
    Закрытие Task тестером. Bug переводится в состояние null, а стаус в состояние COMPLETED.
     */
    @Override
    public Task closeTask(Integer id, String principalUsername) throws WrongStatusException, ObjectNotFoundException, WrongUserException {
        Task task = findById(id);
        if (!task.getUser().equals(userService.findByUsername(principalUsername)))
            throw new WrongUserException(WrongUserExceptionConstants.NOT_THE_CURRENT_ASSIGNEE);
        boolean isAllBugCompleted = task.getBug().stream().allMatch(bug -> bug.getStatus().equals(EStatus.COMPLETED));
        if (isAllBugCompleted || task.getBug() == null){
            task.setStatus(EStatus.COMPLETED);
            taskRepository.save(task);
            return task;
        }
        else
            throw new WrongStatusException("It is impossible to close the task, since it is performed by the developer");
    }
}
