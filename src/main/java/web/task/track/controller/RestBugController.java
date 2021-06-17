package web.task.track.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.task.track.domain.Bug;
import web.task.track.domain.Task;
import web.task.track.dto.BugDto;
import web.task.track.exception.ObjectNotFoundException;
import web.task.track.exception.WrongUserException;
import web.task.track.service.BugService;
import web.task.track.service.TaskService;

import java.security.Principal;

@RestController
@RequestMapping("api/bug")
@Api(description = "Операции с Bug")
public class RestBugController {

    private final BugService bugService;
    private final TaskService taskService;

    @Autowired
    public RestBugController(BugService bugService, TaskService taskService) {
        this.bugService = bugService;
        this.taskService = taskService;
    }

    @PreAuthorize("hasRole('TESTER')")
    @PostMapping("/add")
    public ResponseEntity<?> addBug(@RequestBody BugDto bugDto){
        try {
            Bug bug = bugService.add(bugDto);
            return new ResponseEntity<>(bug, HttpStatus.CREATED);
        } catch (ObjectNotFoundException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('DEVELOPER')")
    @GetMapping("/fix")
    public ResponseEntity<?> fixBug(@RequestParam Integer taskId, @RequestParam Integer bugId,
                                    Principal principal) {
        try {
            Task task = taskService.findById(taskId);
            bugService.fixBug(bugId, task, principal.getName());
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (ObjectNotFoundException | WrongUserException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
