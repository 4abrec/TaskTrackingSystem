package web.task.track.service;


import web.task.track.domain.EStatus;
import web.task.track.domain.Task;
import web.task.track.domain.User;
import web.task.track.dto.AddTaskDto;
import web.task.track.exception.ObjectNotFoundException;
import web.task.track.exception.WrongRoleException;
import web.task.track.exception.WrongStatusException;
import web.task.track.exception.WrongUserException;

import java.util.List;
import java.util.Set;

public interface TaskService {

    Task add(AddTaskDto addTaskDto, String username) throws ObjectNotFoundException, WrongRoleException;

    Task assignToDeveloper(Integer id, String devUsername, String principalUsername) throws ObjectNotFoundException, WrongRoleException, WrongStatusException, WrongUserException;

    Task assignToTester(Integer id, String testerUsername, String principalUsername) throws ObjectNotFoundException, WrongUserException, WrongRoleException, WrongStatusException;

    Task resolveTask(Task task, User user) throws WrongRoleException, WrongStatusException, ObjectNotFoundException, WrongUserException;

    List<Task> getTaskEditHistory(Integer postID);

    List<Task> findAll();

    Task findById(Integer id) throws ObjectNotFoundException;

    Task closeTask(Integer id, String principalUsername) throws WrongStatusException, ObjectNotFoundException, WrongUserException;

    void returnTask(Task task, String principalUsername) throws ObjectNotFoundException, WrongUserException, WrongStatusException;

    Task findByUserAndTitleAndStatus(User user, String title, EStatus status) throws ObjectNotFoundException;

    List<Task> getTasksInFeature(Integer id) throws ObjectNotFoundException;

    void save(Task task);

    void deleteById(Integer id);
}
