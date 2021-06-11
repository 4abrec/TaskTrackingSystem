package web.task.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.task.track.domain.EStatusTask;
import web.task.track.domain.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status,Integer> {
    Status findByName(EStatusTask eStatusTask);
}
