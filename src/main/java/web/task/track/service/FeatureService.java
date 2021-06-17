package web.task.track.service;

import web.task.track.domain.Feature;
import web.task.track.dto.FeatureDto;
import web.task.track.exception.TaskNotCompletedException;
import web.task.track.exception.ObjectNotFoundException;

import java.util.List;

public interface FeatureService {

    Feature add(FeatureDto featureDto) throws ObjectNotFoundException;
    Feature findById(Integer id) throws ObjectNotFoundException;
    List<Feature> findAll();
    Feature closeFeature(Integer id) throws ObjectNotFoundException, TaskNotCompletedException;
    void save(Feature feature);
    void deleteById(Integer id);
}
