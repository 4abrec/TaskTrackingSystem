package web.task.track.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.task.track.domain.User;
import web.task.track.dto.response.JwtResponseDto;
import web.task.track.dto.LoginDto;
import web.task.track.dto.RegistrationDto;
import web.task.track.exception.ObjectNotFoundException;
import web.task.track.service.UserService;

@RestController
@RequestMapping("/api/auth")
@Api(description = "Регистрация и авторизация")
public class RestAuthController {

    private final UserService userService;

    @Autowired
    public RestAuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @ApiOperation(value = "Авторизация")
    public ResponseEntity<?> authUser(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(userService.login(loginDto), HttpStatus.OK);
    }

    @PostMapping("/signup")
    @ApiOperation(value = "Регистрация")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDto registrationDto) {
        try {
            return new ResponseEntity<>(userService.registration(registrationDto), HttpStatus.OK);
        } catch (ObjectNotFoundException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
