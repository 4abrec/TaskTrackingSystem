package web.task.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.task.track.domain.Bug;
import web.task.track.domain.Task;
import java.util.Optional;

@Repository
public interface BugRepository extends JpaRepository<Bug, Integer> {
    Optional<Bug> findByIdAndTask(Integer id, Task task);
}
