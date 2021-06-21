package web.task.track.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import web.task.track.IntegrationTestBase;
import web.task.track.domain.EStatus;
import web.task.track.domain.Feature;
import web.task.track.domain.Task;
import web.task.track.domain.User;
import web.task.track.dto.AddTaskDto;
import web.task.track.exception.ObjectNotFoundException;
import web.task.track.exception.WrongRoleException;
import web.task.track.exception.WrongStatusException;
import web.task.track.exception.WrongUserException;
import web.task.track.repository.FeatureRepository;
import web.task.track.repository.TaskRepository;
import web.task.track.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest extends IntegrationTestBase {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeatureRepository featureRepository;


    @Test
    void findByIdTest() throws ObjectNotFoundException {
        Task task = taskService.findById(5);
        assertNotNull(task);
    }

    @Test
    void findAllTest() {
        List<Task> tasks = taskService.findAll();
        assertNotEquals(0, tasks.size());
    }

    @Test
    void saveTest() {
        Feature feature = featureRepository.findById(6).orElse(null);
        User user = userRepository.findById(4).orElse(null);
        assertNotNull(feature);
        assertNotNull(user);
        Task task = new Task("testTask", "test", user, feature, EStatus.OPEN);
        taskService.save(task);
        assertNotNull(task.getId());
    }


    @Test
    void findByUserAndTitleAndStatusTest() throws ObjectNotFoundException {
        User user = userRepository.findById(8).orElse(null);
        assertNotNull(user);
        Task task = taskService.findByUserAndTitleAndStatus(user, "testTask", EStatus.RESOLVED);
        assertNotNull(task);
        assertEquals(8, task.getUser().getId());
        assertEquals("testTask", task.getTitle());
        assertEquals(EStatus.RESOLVED, task.getStatus());
    }


    @Test
    void getTaskEditHistoryTest() throws ObjectNotFoundException, WrongStatusException, WrongRoleException, WrongUserException {
        taskService.assignToDeveloper(1, "5abrec", "2abrec");
        List<Task> tasks = taskService.getTaskEditHistory(1);
        assertNotNull(tasks);
    }

    @Test
    void addTaskTest() throws ObjectNotFoundException, WrongRoleException {
        Task task = taskService.add(new AddTaskDto("testTask100", "test100", 1), "2abrec");
        Feature feature = featureRepository.findById(1).orElse(null);
        assertNotNull(feature);
        assertNotNull(task.getId());
        assertEquals("testTask100", task.getTitle());
        assertEquals("test100", task.getDescription());
        assertEquals(feature, task.getFeature());
        assertEquals("2abrec", task.getUser().getUsername());
    }

    @Test
    void taskInFeatureTest() throws ObjectNotFoundException {
        List<Task> tasks = taskService.getTasksInFeature(4);
        assertEquals(4, tasks.size());
        Task task = tasks.get(0);
        assertEquals("testTask", task.getTitle());
        assertEquals("test", task.getDescription());
    }

    @Test
    void resolveTask() throws ObjectNotFoundException, WrongStatusException, WrongRoleException, WrongUserException {
        User user = userRepository.findById(5).orElse(null);
        Task task = taskRepository.findById(6).orElse(null);
        assertNotNull(user);
        assertNotNull(task);
        Task resolveTask = taskService.resolveTask(task, user);
        assertEquals(EStatus.RESOLVED, resolveTask.getStatus());
    }

    @Test
    void assignToTesterTest() throws ObjectNotFoundException, WrongStatusException, WrongUserException, WrongRoleException {
        Task task = taskService.assignToTester(7, "8abrec", "5abrec");
        assertEquals("8abrec", task.getUser().getUsername());
    }

    @Test
    void closeFeature() throws ObjectNotFoundException, WrongStatusException, WrongUserException {
        Task task = taskService.closeTask(8, "8abrec");
        assertEquals(EStatus.COMPLETED, task.getStatus());
    }
}
