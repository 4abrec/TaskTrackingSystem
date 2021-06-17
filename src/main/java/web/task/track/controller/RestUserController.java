package web.task.track.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.task.track.domain.User;
import web.task.track.exception.ObjectNotFoundException;
import web.task.track.service.UserService;
import java.util.List;

@RestController
@RequestMapping("api/user")
@Api(description = "Операции с пользователями")
public class RestUserController {

    private final UserService userService;

    @Autowired
    public RestUserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Поиск пользователя по id")
    public ResponseEntity<?> getUser(@PathVariable Integer id){
        try {
            User user = userService.findById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (ObjectNotFoundException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('DEVELOPER')")
    @GetMapping
    @ApiOperation(value = "Получение всех юзеров")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("ADMIN")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Удаление юзера. Доступно только администратору")
    public void deleteUser(@PathVariable Integer id){
        userService.deleteById(id);
    }
}
