package web.task.track.service;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import web.task.track.IntegrationTestBase;
import web.task.track.domain.EStatus;
import web.task.track.domain.Feature;
import web.task.track.domain.User;
import web.task.track.dto.FeatureDto;
import web.task.track.exception.ObjectNotFoundException;
import web.task.track.exception.TaskNotCompletedException;
import web.task.track.exception.WrongUserException;
import web.task.track.repository.UserRepository;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class FeatureServiceTest extends IntegrationTestBase {


    @Autowired
    private FeatureService featureService;

    @Autowired
    private UserRepository userRepository;


    @Test
    void addFeatureTest() throws ObjectNotFoundException {
        FeatureDto featureDto = new FeatureDto("testFeature100", "test100", Set.of("4abrec"));
        Feature feature = featureService.add(featureDto);
        assertNotNull(feature.getId());
        assertEquals("testFeature100", feature.getTitle());
    }

    @Test
    void closeFeatureTest() throws ObjectNotFoundException, TaskNotCompletedException, WrongUserException {
        Feature feature = featureService.closeFeature(5, "9abrec");
        assertEquals(EStatus.COMPLETED, feature.getStatusFeature());
    }

    @Test
    void findByIdTest() throws ObjectNotFoundException {
        Feature feature = featureService.findById(6);
        assertNotNull(feature);
    }

    @Test
    void findAllTest() {
        List<Feature> features = featureService.findAll();
        assertNotEquals(0, features.size());
    }

    @Test
    void saveTest() throws ObjectNotFoundException {
        User user = userRepository.findById(4).orElse(null);
        assertNotNull(user);
        Feature feature = new Feature("testFeature", "test", Set.of(user));
        featureService.save(feature);
        assertNotNull(feature.getId());
    }
}
