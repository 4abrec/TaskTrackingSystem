package web.task.track.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.task.track.domain.Status;
import web.task.track.service.StatusService;
import java.util.List;

@RestController
@RequestMapping("api/status")
public class RestStatusController {

    private final StatusService statusService;

    @Autowired
    public RestStatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Status> getStatus(@PathVariable Integer id){
        Status status = statusService.findById(id);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Status>> getAllStatus(){
        List<Status> statuses = statusService.findAll();
        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable Integer id){
        statusService.deleteById(id);
    }
}
