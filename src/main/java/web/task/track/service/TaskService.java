package web.task.track.service;

import org.springframework.http.ResponseEntity;
import web.task.track.domain.EStatus;
import web.task.track.domain.Task;
import web.task.track.domain.User;
import web.task.track.dto.BugDto;
import web.task.track.dto.AddTaskDto;

import java.util.List;
import java.util.Set;

public interface TaskService {

    ResponseEntity<Task> add(AddTaskDto addTaskDto);
    Task resolveTask(Task task, User user);
    List<Task> getTaskEditHistory(Integer postID);
    List<Task> findAll();
    Task findById(Integer id);
    Task closeTask(Integer id);
    Task returnTask(BugDto bugDto);
    Task findByUserAndTitleAndStatus(User user, String title, EStatus status);
    Set<Task> inFeature(Integer id);
    void save(Task task);
    void deleteById(Integer id);
}
