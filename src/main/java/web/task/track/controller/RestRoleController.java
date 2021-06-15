package web.task.track.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.task.track.domain.Role;
import web.task.track.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@Api(description = "Операции с ролями")
public class RestRoleController {

    private final RoleService roleService;

    @Autowired
    public RestRoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Получение роли по id")
    public ResponseEntity<Role> findById(@PathVariable Integer id){
        Role role = roleService.findById(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "Получение всех ролей")
    public ResponseEntity<List<Role>> getAll(){
        List<Role> roles = roleService.getAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Удаление роли. Доступно только администратору")
    public void deleteById(@PathVariable Integer id){
        roleService.deleteById(id);
    }
}
