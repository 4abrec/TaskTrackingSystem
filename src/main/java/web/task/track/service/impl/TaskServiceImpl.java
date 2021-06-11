package web.task.track.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import web.task.track.domain.*;
import web.task.track.dto.BugDto;
import web.task.track.dto.TaskDto;
import web.task.track.repository.BugRepository;
import web.task.track.repository.RoleRepository;
import web.task.track.repository.StatusRepository;
import web.task.track.repository.TaskRepository;
import web.task.track.service.FeatureService;
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
    private final StatusRepository statusRepository;
    private final BugRepository bugRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           FeatureService featureService,
                           StatusRepository statusRepository,
                           BugRepository bugRepository,
                           RoleRepository roleRepository,
                           UserService userService) {
        this.taskRepository = taskRepository;
        this.featureService = featureService;
        this.statusRepository = statusRepository;
        this.bugRepository = bugRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    /*
    Получении истории смены текущего исполнителя у задания.
     */
    @Override
    public List<Task> getPostEditHistory(Integer postID) {

        List<Task> historyList = new ArrayList<>();
        taskRepository.findRevisions(postID).get().forEach(task -> {
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
    public Task findById(Integer id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task Not Found with id: " + id));
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
    public Set<Task> inFeature(Integer id) {
        Feature feature = featureService.findById(id);
        return feature.getTasks();
    }

    /*
        Создание Task. Она сразу переводится на DEVELOPER, Status переводится в IN_PROGRESS.
         */
    @Override
    public ResponseEntity<Task> add(TaskDto taskDto) {
        User user = userService.findByUsername(taskDto.getUsername());
        Role role = roleRepository.findByName(ERole.ROLE_DEVELOPER)
                .orElseThrow(() -> new RuntimeException("Error, Role DEVELOPER is not found"));
        if (!user.getRoles().contains(role))
            throw new RuntimeException("This user is not a developer");
        Feature feature = featureService.findById(taskDto.getFeatureId());
        if (!feature.getUsers().contains(user))
            throw new RuntimeException("The user is not involved in the development feature");
        Status status = statusRepository.findByName(EStatusTask.IN_PROGRESS);
        Task task = new Task(taskDto.getTitle(), taskDto.getDescription(), user, feature, status);
        taskRepository.save(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /*
    Решение задачи юзером с ролью developer. Статус переводится в RESOLVED, а текущим юзером
    становится TESTER, который принимает учатие в разработке.
     */
    @Override
    public Task resolveTask(Task task, User user) {
        Role currentRole = roleRepository.findByName(ERole.ROLE_DEVELOPER)
                    .orElseThrow(() -> new RuntimeException("Error, Role DEVELOPER is not found"));
        if(task.getUser().getRoles().contains(currentRole)){
            Status status = statusRepository.findByName(EStatusTask.RESOLVED);
            Feature feature = task.getFeature();
            Role nextRole = roleRepository.findByName(ERole.ROLE_TESTER)
                    .orElseThrow(() -> new RuntimeException("Error, Role TESTER is not found"));
            if (!user.getRoles().contains(nextRole))
                throw new RuntimeException("This user is not a tester");
            if (!feature.getUsers().contains(user))
                throw new RuntimeException("The user is not involved in the development feature");
            task.setStatus(status);
            task.setUser(user);
            taskRepository.save(task);
            return  task;
        }
        else
            throw new RuntimeException("It is impossible to solve the task, since it is not under development");
    }

    /*
    Возврат тестером задания на предыдущего developer. Из аудиторской таблицы выбираются
    записи конкретной task. Затем из этой выборки идет получение записей, в которых
    текущий юзер - developer. Task возвращается последнему developer, который с ней работал.
    Также создается Bug для данной Task и ее статус переводится обратно в IN_PROGRESS.
     */
    @Override
    public Task returnTask(BugDto bugDto) {
        Task bugTask = findById(bugDto.getTaskId());
        Status status = statusRepository.findByName(EStatusTask.IN_PROGRESS);
        Bug bug = new Bug(bugDto.getTitle(), bugDto.getDescription(), bugTask);
        bugRepository.save(bug);
        List<Task> historyTasks = getPostEditHistory(bugDto.getTaskId());
        Role developerRole = roleRepository.findByName(ERole.ROLE_DEVELOPER)
                .orElseThrow(() -> new RuntimeException("Error, Role DEVELOPER is not found"));
        List<Task> tasks = historyTasks
                .stream()
                .filter(task -> task.getUser().getRoles().contains(developerRole))
                .collect(Collectors.toList());
        Task historyTask = tasks.get(tasks.size() - 1);
        User user = userService.findById(historyTask.getUser().getId());
        bugTask.setUser(user);
        bugTask.setStatus(status);
        bugTask.setBug(bug);
        save(bugTask);
        return bugTask;
    }

    /*
    Закрытие Task тестером. Bug переводится в состояние null, а стаус в состояние COMPLETED.
     */
    @Override
    public Task closeTask(Integer id) {
        Task task = findById(id);
        Role nextRole = roleRepository.findByName(ERole.ROLE_TESTER)
                .orElseThrow(() -> new RuntimeException("Error, Role TESTER is not found"));
        if(task.getUser().getRoles().contains(nextRole)){
            Status status = statusRepository.findByName(EStatusTask.COMPLETED);
            task.setBug(null);
            task.setStatus(status);
            save(task);
            return task;
        }
        else
            throw new RuntimeException("It is impossible to close the task, since it is performed by the developer");
    }
}
