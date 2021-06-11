package web.task.track.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import web.task.track.domain.Task;
import web.task.track.domain.User;
import web.task.track.dto.BugDto;
import web.task.track.dto.TaskDto;
import web.task.track.service.TaskService;
import web.task.track.service.UserService;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/task")
public class RestTaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public RestTaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    /*
    Добавление новой task.
     */
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@Validated @RequestBody TaskDto taskDto) {
        return taskService.add(taskDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Integer id){
        taskService.deleteById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Integer id)  {
        Task task = taskService.findById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        List<Task> tasks = taskService.findAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /*
    Получение всех tasks в конкретной feature.
     */
    @GetMapping("/feature/{id}")
    public ResponseEntity<Set<Task>> getTasksInFeature(@PathVariable Integer id){
        Set<Task> tasks = taskService.inFeature(id);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /*
    Выполнение task девелопером.
     */
    @PreAuthorize("hasRole('DEVELOPER')")
    @GetMapping("/resolve")
    public ResponseEntity<Task> resolveTask(@RequestParam Integer id,
                                            @RequestParam String username) {
        Task task = taskService.findById(id);
        User user = userService.findByUsername(username);
        taskService.resolveTask(task, user);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /*
    Возврат task тестером.
     */
    @PreAuthorize("hasRole('TESTER')")
    @PostMapping("/return")
    public ResponseEntity<Task> returnTask(@RequestBody @Validated BugDto bugDto){
        Task task = taskService.returnTask(bugDto);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
    /*
    Закрытие task тестером.
     */
    @PreAuthorize("hasRole('TESTER')")
    @GetMapping("/close/{id}")
    public ResponseEntity<Task> closeTask(@PathVariable Integer id){
        Task task = taskService.closeTask(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}
