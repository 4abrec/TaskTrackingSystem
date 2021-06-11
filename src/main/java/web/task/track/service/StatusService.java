package web.task.track.service;

import web.task.track.domain.EStatusTask;
import web.task.track.domain.Status;
import java.util.List;


public interface StatusService {
    Status findById(Integer id);
    Status findByName(EStatusTask statusTask);
    List<Status> findAll();
    void deleteById(Integer id);
    void save(Status status);
}
