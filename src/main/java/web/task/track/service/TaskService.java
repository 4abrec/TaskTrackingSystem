package web.task.track.service;

import org.springframework.http.ResponseEntity;
import web.task.track.domain.Task;
import web.task.track.domain.User;
import web.task.track.dto.BugDto;
import web.task.track.dto.TaskDto;

import java.util.List;
import java.util.Set;

public interface TaskService {

    ResponseEntity<Task> add(TaskDto taskDto);
    Task resolveTask(Task task, User user);
    List<Task> getPostEditHistory(Integer postID);
    List<Task> findAll();
    Task findById(Integer id);
    Task closeTask(Integer id);
    Task returnTask(BugDto bugDto);
    Set<Task> inFeature(Integer id);
    void save(Task task);
    void deleteById(Integer id);
}
