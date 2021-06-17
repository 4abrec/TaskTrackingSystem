package web.task.track.exception;

public class WrongUserException extends Exception{
    public WrongUserException(String message) {
        super(message);
    }
}
