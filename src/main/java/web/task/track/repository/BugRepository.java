package web.task.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.task.track.domain.Bug;

@Repository
public interface BugRepository extends JpaRepository<Bug, Integer> {
}
