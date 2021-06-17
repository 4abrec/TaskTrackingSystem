package web.task.track.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.task.track.domain.*;
import web.task.track.dto.FeatureDto;
import web.task.track.exception.TaskNotCompletedException;
import web.task.track.exception.ObjectNotFoundException;
import web.task.track.repository.FeatureRepository;
import web.task.track.repository.UserRepository;
import web.task.track.service.FeatureService;
import web.task.track.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public FeatureServiceImpl(FeatureRepository featureRepository,
                              UserRepository userRepository,
                              UserService userService) {
        this.featureRepository = featureRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /*
    Создание Feature. При создании в Feature добавляются юзеры, которые
    участвуют в разработке.
     */
    @Override
    public Feature add(FeatureDto featureDto) throws ObjectNotFoundException {
        Set<User> featureUsers = new HashSet<>();
        for(String username: featureDto.getUsers()){
            if (userRepository.existsByUsername(username)) {
                featureUsers.add(userService.findByUsername(username));
            }
            else
                throw new ObjectNotFoundException("User not found with username " + username);
        }

        Feature feature = new Feature(featureDto.getTitle(), featureDto.getDescription(), featureUsers);
        featureRepository.save(feature);
        featureUsers.forEach(user -> {
            user.getFeatures().add(feature);
            userService.save(user);
            }
        );
        return feature;
    }

    /*
    Закрытие Feature. Сначала проверяется, все ли таски выполнены.
     */
    @Override
    public Feature closeFeature(Integer id) throws ObjectNotFoundException, TaskNotCompletedException {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("This feature does not exist"));
        boolean isCompleted = feature.getTasks()
                .stream()
                .allMatch(task -> task.getStatus().equals(EStatus.COMPLETED));
        if (isCompleted){
            feature.setStatusFeature(EStatus.COMPLETED);
            save(feature);
            return feature;
        }
        else
            throw new TaskNotCompletedException("Not all tasks completed");

    }

    @Override
    public Feature findById(Integer id) throws ObjectNotFoundException {
        return featureRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Feature not found with id " + id));
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
