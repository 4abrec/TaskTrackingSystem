package web.task.track.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import web.task.track.IntegrationTestBase;
import web.task.track.domain.ERole;
import web.task.track.domain.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoleServiceTest extends IntegrationTestBase {

    @Autowired
    private RoleService roleService;

    @Test
    void findByIdTest() {
        assertNotNull(roleService.findById(1));
    }

    @Test
    void findByNameTest() {
        assertNotNull(roleService.findByName(ERole.ROLE_ADMIN));
    }

    @Test
    void findAllTest() {
        List<Role> roles = roleService.findAll();
        assertNotEquals(0, roles.size());
    }

}
