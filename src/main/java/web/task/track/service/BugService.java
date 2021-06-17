package web.task.track.service;

import web.task.track.domain.Bug;
import web.task.track.domain.Task;
import web.task.track.dto.BugDto;
import web.task.track.exception.ObjectNotFoundException;
import web.task.track.exception.WrongUserException;

public interface BugService {
    Bug add(BugDto bugDto) throws ObjectNotFoundException;
    Bug findByIdAndTask(Integer id, Task task) throws ObjectNotFoundException;
    void fixBug(Integer bugId, Task task, String principalUsername) throws ObjectNotFoundException, WrongUserException;
}
