package web.task.track.service.impl;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import web.task.track.domain.*;
import web.task.track.dto.FeatureDto;
import web.task.track.repository.FeatureRepository;
import web.task.track.repository.UserRepository;
import web.task.track.service.FeatureService;
import web.task.track.service.StatusService;
import web.task.track.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;
    private final UserRepository userRepository;
    private final StatusService statusService;
    private final UserService userService;

    @Autowired
    public FeatureServiceImpl(FeatureRepository featureRepository,
                              UserRepository userRepository,
                              StatusService statusService,
                              UserService userService) {
        this.featureRepository = featureRepository;
        this.userRepository = userRepository;
        this.statusService = statusService;
        this.userService = userService;
    }

    /*
    Создание Feature. При создании в Feature добавляются юзеры, которые
    участвуют в разработке.
     */
    @Override
    public ResponseEntity<Feature> add(FeatureDto featureDto) {
        Set<User> featureUsers = new HashSet<>();
        featureDto.getUsers().forEach(user -> {
            if (userRepository.existsByUsername(user)) {
                featureUsers.add(userService.findByUsername(user));
            }
            else try {
                throw new NotFoundException("User not found with username " + user);
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        });
        Feature feature = new Feature(featureDto.getTitle(), featureDto.getDescription(), featureUsers);
        featureRepository.save(feature);
        featureUsers.forEach(user -> {
            user.getFeatures().add(feature);
            userService.save(user);
            }
        );
        return new ResponseEntity<>(feature, HttpStatus.OK);
    }

    /*
    Закрытие Feature. Сначала проверяется, все ли таски выполнены.
     */
    @Override
    public Feature closeFeature(Integer id) {
        Status status = statusService.findByName(EStatusTask.COMPLETED);
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This feature does not exist"));
        boolean isCompleted = feature.getTasks()
                .stream()
                .allMatch(task -> task.getStatus().equals(status));
        if (isCompleted){
            feature.setStatusFeature(EStatusFeature.COMPLETED);
            save(feature);
            return feature;
        }
        else
            throw new RuntimeException("Not all tasks completed");

    }

    @Override
    public Feature findById(Integer id) {
        return featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found with id " + id));
    }

    @Override
    public List<Feature> findAll() {
        return featureRepository.findAll();
    }

    @Override
    public void save(Feature feature) {
        featureRepository.save(feature);
    }

    @Override
    public void deleteById(Integer id) {
        featureRepository.deleteById(id);
    }
}
