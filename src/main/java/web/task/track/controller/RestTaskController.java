package web.task.track.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import web.task.track.domain.Task;
import web.task.track.domain.User;
import web.task.track.dto.BugDto;
import web.task.track.dto.AddTaskDto;
import web.task.track.dto.FindTaskDto;
import web.task.track.service.TaskService;
import web.task.track.service.UserService;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/task")
@Api(description = "Операции с task")
public class RestTaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public RestTaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @ApiOperation(value = "Добавление задачи. Дочтупно только менеджеру")
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@Validated @RequestBody AddTaskDto addTaskDto) {
        return taskService.add(addTaskDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Удаление задачи. Доступно только администратору или менеджеру")
    public void deleteTask(@PathVariable Integer id){
        taskService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Получение задачи по id")
    public ResponseEntity<Task> getTask(@PathVariable Integer id)  {
        Task task = taskService.findById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "Получение всех задач")
    public ResponseEntity<List<Task>> getAllTasks(){
        List<Task> tasks = taskService.findAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("/find")
    @ApiOperation(value = "Поиск задачи по пользователю, названию и статусу")
    public ResponseEntity<Task> getTaskByUserAndTitleAndStatus(@RequestBody @Validated FindTaskDto findTaskDto){
        User user = userService.findByUsername(findTaskDto.getUsername());
        Task task = taskService.findByUserAndTitleAndStatus(user, findTaskDto.getTitle(), findTaskDto.getStatus());
        return new ResponseEntity<>(task, HttpStatus.OK);
    }


    @GetMapping("/feature/{id}")
    @ApiOperation(value = "Получение всех задач в конкретной feature")
    public ResponseEntity<Set<Task>> getTasksInFeature(@PathVariable Integer id){
        Set<Task> tasks = taskService.inFeature(id);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('DEVELOPER')")
    @GetMapping("/resolve")
    @ApiOperation(value = "Выполнение задачи девелопером")
    public ResponseEntity<Task> resolveTask(@RequestParam Integer id,
                                            @RequestParam String username) {
        Task task = taskService.findById(id);
        User user = userService.findByUsername(username);
        taskService.resolveTask(task, user);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('TESTER')")
    @PostMapping("/return")
    @ApiOperation(value = "Возврат задачи тестером на доработку")
    public ResponseEntity<Task> returnTask(@RequestBody @Validated BugDto bugDto){
        Task task = taskService.returnTask(bugDto);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TESTER')")
    @GetMapping("/close/{id}")
    @ApiOperation(value = "Закрытие задачи тестером")
    public ResponseEntity<Task> closeTask(@PathVariable Integer id){
        Task task = taskService.closeTask(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}
