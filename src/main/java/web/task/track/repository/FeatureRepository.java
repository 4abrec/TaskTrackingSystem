package web.task.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.task.track.domain.Feature;


@Repository
public interface FeatureRepository extends JpaRepository<Feature, Integer> {

}
