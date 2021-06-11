package web.task.track.service;

import org.springframework.http.ResponseEntity;
import web.task.track.domain.User;
import web.task.track.dto.response.JwtResponse;
import web.task.track.dto.LoginDto;
import web.task.track.dto.response.MessageResponse;
import web.task.track.dto.RegistrationDto;

import java.util.List;

public interface UserService {

    User findById(Integer id);
    User findByUsername(String username);
    List<User> findAll();
    ResponseEntity<JwtResponse> login(LoginDto loginDto);
    ResponseEntity<MessageResponse> registration(RegistrationDto registrationDto);
    void save(User user);
    void deleteById(Integer id);
}
