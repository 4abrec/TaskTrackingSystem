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
import web.task.track.dto.AddTaskDto;
import web.task.track.dto.FindTaskDto;
import web.task.track.exception.ObjectNotFoundException;
import web.task.track.exception.WrongRoleException;
import web.task.track.exception.WrongStatusException;
import web.task.track.exception.WrongUserException;
import web.task.track.service.TaskService;
import web.task.track.service.UserService;

import java.security.Principal;
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

    @ApiOperation(value = "Добавление задачи. Доступно только менеджеру")
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/add")
    public ResponseEntity<?> addTask(@Validated @RequestBody AddTaskDto addTaskDto, Principal principal) {
        try {
            Task task = taskService.add(addTaskDto, principal.getName());
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (ObjectNotFoundException | WrongRoleException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Назанчение задачи на девелопера. Доступно только менеджеру")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/assign/dev")
    public ResponseEntity<?> assignToDeveloper(@RequestParam Integer taskId, @RequestParam String devUsername,
                                               Principal principal) {
        try {
            Task task = taskService.assignToDeveloper(taskId, devUsername, principal.getName());
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (ObjectNotFoundException | WrongRoleException | WrongStatusException | WrongUserException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('DEVELOPER')")
    @GetMapping("/resolve/{id}")
    @ApiOperation(value = "Выполнение задачи девелопером")
    public ResponseEntity<?> resolveTask(@PathVariable Integer id, Principal principal) {
        try {
            Task task = taskService.findById(id);
            User user = userService.findByUsername(principal.getName());
            taskService.resolveTask(task, user);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (ObjectNotFoundException | WrongStatusException | WrongRoleException | WrongUserException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Назанчение задачи на тестера. Доступно только девелоперу")
    @PreAuthorize("hasRole('DEVELOPER')")
    @GetMapping("/assign/tester")
    public ResponseEntity<?> assignToTester(@RequestParam Integer taskId, @RequestParam String devTester,
                                            Principal principal) {
        try {
            Task task = taskService.assignToTester(taskId, devTester, principal.getName());
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (ObjectNotFoundException | WrongRoleException | WrongStatusException | WrongUserException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('TESTER')")
    @GetMapping("/return/{id}")
    @ApiOperation(value = "Возврат задачи тестером на доработку")
    public ResponseEntity<?> returnTask(@PathVariable Integer id, Principal principal) {
        try {
            Task task = taskService.findById(id);
            taskService.returnTask(task, principal.getName());
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (ObjectNotFoundException | WrongStatusException | WrongUserException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('TESTER')")
    @GetMapping("/close/{id}")
    @ApiOperation(value = "Закрытие задачи тестером")
    public ResponseEntity<?> closeTask(@PathVariable Integer id, Principal principal) {
        try {
            Task task = taskService.closeTask(id, principal.getName());
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (WrongStatusException | ObjectNotFoundException | WrongUserException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Удаление задачи. Доступно только администратору или менеджеру")
    public void deleteTask(@PathVariable Integer id) {
        taskService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Получение задачи по id")
    public ResponseEntity<?> getTask(@PathVariable Integer id) {

        try {
            Task task = taskService.findById(id);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (ObjectNotFoundException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    @ApiOperation(value = "Получение всех задач")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.findAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("/find")
    @ApiOperation(value = "Поиск задачи по пользователю, названию и статусу")
    public ResponseEntity<?> getTaskByUserAndTitleAndStatus(@RequestBody @Validated FindTaskDto findTaskDto) {

        try {
            User user = userService.findByUsername(findTaskDto.getUsername());
            Task task = taskService.findByUserAndTitleAndStatus(user, findTaskDto.getTitle(), findTaskDto.getStatus());
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (ObjectNotFoundException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/feature/{id}")
    @ApiOperation(value = "Получение всех задач в конкретной feature")
    public ResponseEntity<?> getTasksInFeature(@PathVariable Integer id) {
        try {
            List<Task> tasks = taskService.getTasksInFeature(id);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (ObjectNotFoundException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
