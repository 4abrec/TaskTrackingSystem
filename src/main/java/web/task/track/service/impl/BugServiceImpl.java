package web.task.track.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.task.track.domain.Bug;
import web.task.track.domain.EStatus;
import web.task.track.domain.Task;
import web.task.track.dto.BugDto;
import web.task.track.exception.ObjectNotFoundException;
import web.task.track.exception.WrongUserException;
import web.task.track.exception.constant.WrongUserExceptionConstants;
import web.task.track.repository.BugRepository;
import web.task.track.service.BugService;
import web.task.track.service.TaskService;
import web.task.track.service.UserService;

@Service
public class BugServiceImpl implements BugService {

    private final TaskService taskService;
    private final UserService userService;
    private final BugRepository bugRepository;

    @Autowired
    public BugServiceImpl(TaskService taskService, UserService userService, BugRepository bugRepository) {
        this.taskService = taskService;
        this.userService = userService;
        this.bugRepository = bugRepository;
    }

    @Override
    public Bug add(BugDto bugDto) throws ObjectNotFoundException {
        Task task = taskService.findById(bugDto.getTaskId());
        Bug bug = new Bug(task.getTitle(), task.getDescription(), EStatus.OPEN, task);
        bugRepository.save(bug);
        return bug;
    }

    @Override
    public Bug findByIdAndTask(Integer id, Task task) throws ObjectNotFoundException {
        return bugRepository.findByIdAndTask(id, task)
                .orElseThrow(() -> new ObjectNotFoundException("Bug with id: " + id + " and Task: " + task +" is not found"));
    }

    @Override
    public void fixBug(Integer bugId, Task task, String principalUsername) throws ObjectNotFoundException, WrongUserException {
        if (!task.getUser().equals(userService.findByUsername(principalUsername)))
            throw new WrongUserException(WrongUserExceptionConstants.NOT_THE_CURRENT_ASSIGNEE);
        Bug bug = findByIdAndTask(bugId, task);
        bug.setStatus(EStatus.COMPLETED);
        bugRepository.save(bug);
        task.setStatus(EStatus.RESOLVED);
        taskService.save(task);
    }
}
