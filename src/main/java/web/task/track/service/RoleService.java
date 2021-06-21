package web.task.track.service;

import web.task.track.domain.ERole;
import web.task.track.domain.Role;

import java.util.List;

public interface RoleService {
    Role findByName(ERole name);

    Role findById(Integer id);

    List<Role> findAll();

    void deleteById(Integer id);
}
