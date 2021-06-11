package web.task.track.service;

import org.springframework.http.ResponseEntity;
import web.task.track.domain.Feature;
import web.task.track.dto.FeatureDto;

import java.util.List;

public interface FeatureService {

    ResponseEntity<Feature> add(FeatureDto featureDto);
    Feature findById(Integer id);
    List<Feature> findAll();
    Feature closeFeature(Integer id);
    void save(Feature feature);
    void deleteById(Integer id);
}
