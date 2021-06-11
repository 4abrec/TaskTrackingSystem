package web.task.track.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.task.track.dto.response.JwtResponse;
import web.task.track.dto.LoginDto;
import web.task.track.dto.response.MessageResponse;
import web.task.track.dto.RegistrationDto;
import web.task.track.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class RestAuthController {

    private final UserService userService;

    @Autowired
    public RestAuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authUser(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody RegistrationDto registrationDto) {
        return userService.registration(registrationDto);
    }
}
