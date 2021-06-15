package web.task.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import web.task.track.domain.EStatus;
import web.task.track.domain.Task;
import web.task.track.domain.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>, RevisionRepository<Task, Integer, Integer> {
        Task findByUserAndTitleAndStatus(User user, String title, EStatus status);
}
