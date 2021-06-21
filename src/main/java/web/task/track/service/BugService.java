package web.task.track.service;

import web.task.track.domain.Bug;
import web.task.track.domain.Task;
import web.task.track.dto.BugDto;
import web.task.track.exception.ObjectNotFoundException;
import web.task.track.exception.WrongUserException;

import java.util.List;

public interface BugService {
    Bug add(BugDto bugDto) throws ObjectNotFoundException;

    Bug findByIdAndTask(Integer id, Task task) throws ObjectNotFoundException;

    Bug fixBug(Integer bugId, Task task, String principalUsername) throws ObjectNotFoundException, WrongUserException;

    Bug findById(Integer id) throws ObjectNotFoundException;

    List<Bug> findAll();

    void save(Bug bug);

    void deleteById(Integer id);
}
