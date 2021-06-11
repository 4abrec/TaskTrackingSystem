package web.task.track.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.task.track.domain.EStatusTask;
import web.task.track.domain.Status;
import web.task.track.repository.StatusRepository;
import web.task.track.service.StatusService;
import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Autowired
    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public Status findById(Integer id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status Not Found with id: " + id));
    }

    @Override
    public List<Status> findAll() {
        return statusRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        statusRepository.deleteById(id);
    }

    @Override
    public void save(Status status) {
        statusRepository.save(status);
    }

    @Override
    public Status findByName(EStatusTask statusTask) {
        return statusRepository.findByName(statusTask);
    }
}
