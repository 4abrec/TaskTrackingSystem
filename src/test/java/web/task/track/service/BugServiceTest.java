package web.task.track.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import web.task.track.IntegrationTestBase;
import web.task.track.domain.Bug;
import web.task.track.domain.EStatus;
import web.task.track.domain.Task;
import web.task.track.dto.BugDto;
import web.task.track.exception.ObjectNotFoundException;
import web.task.track.exception.WrongUserException;
import web.task.track.repository.TaskRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class BugServiceTest extends IntegrationTestBase {

    @Autowired
    private BugService bugService;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    void findByIdTest() throws ObjectNotFoundException {
        Bug bug = bugService.findById(1);
        assertNotNull(bug);
    }

    @Test
    void findAllTest() {
        List<Bug> bugs = bugService.findAll();
        assertNotEquals(0, bugs.size());
    }

    @Test
    void save() {
        Task task = taskRepository.findById(13).orElse(null);
        assertNotNull(task);
        Bug bug = new Bug("testBug", "test", EStatus.OPEN, task);
        bugService.save(bug);
        assertNotNull(bug.getId());
    }


    @Test
    void addBugTest() throws ObjectNotFoundException {
        Bug bug = bugService.add(new BugDto("testBug", "test", 11));
        Task task = taskRepository.findById(11).orElse(null);
        assertNotNull(task);
        assertEquals("testBug", bug.getTitle());
        assertEquals("test", bug.getDescription());
        assertEquals(EStatus.OPEN, bug.getStatus());
        assertEquals(task, bug.getTask());
    }

    @Test
    void fixBugTest() throws ObjectNotFoundException, WrongUserException {
        Task task = taskRepository.findById(12).orElse(null);
        assertNotNull(task);
        Bug bug = bugService.fixBug(1, task, "6abrec");
        assertEquals(EStatus.COMPLETED, bug.getStatus());
        assertEquals(EStatus.RESOLVED, task.getStatus());
    }

}
