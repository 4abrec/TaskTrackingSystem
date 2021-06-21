package web.task.track.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import web.task.track.IntegrationTestBase;
import web.task.track.domain.User;
import web.task.track.exception.ObjectNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest extends IntegrationTestBase {

    @Autowired
    private UserService userService;

    @Test
    void findByIdTest() throws ObjectNotFoundException {
        User user = userService.findById(1);
        assertNotNull(user);
    }

    @Test
    void findAll() {
        List<User> users = userService.findAll();
        assertNotEquals(0, users.size());
    }

    @Test
    void saveTest() {
        User user = new User("100abrec", "100abrec@gmail.com", "1234", "Artyom", "Cherkasov");
        userService.save(user);
        assertNotNull(user.getId());
    }

}
