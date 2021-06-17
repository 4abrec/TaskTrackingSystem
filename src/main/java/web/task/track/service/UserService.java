package web.task.track.service;

import web.task.track.domain.Task;
import web.task.track.domain.User;
import web.task.track.dto.response.JwtResponseDto;
import web.task.track.dto.LoginDto;
import web.task.track.dto.response.MessageResponseDto;
import web.task.track.dto.RegistrationDto;
import web.task.track.exception.ObjectNotFoundException;
import java.util.List;


public interface UserService {

    User findById(Integer id) throws ObjectNotFoundException;
    User findByUsername(String username) throws ObjectNotFoundException;
    List<User> findAll();
    List<User> findUserInTaskEditHistory(List<Task> historyTask) throws ObjectNotFoundException;
    JwtResponseDto login(LoginDto loginDto);
    MessageResponseDto registration(RegistrationDto registrationDto) throws ObjectNotFoundException;
    void save(User user);
    void deleteById(Integer id);
}
