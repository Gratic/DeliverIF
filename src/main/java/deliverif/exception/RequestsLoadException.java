package deliverif.exception;

public class RequestsLoadException extends Exception {
    public RequestsLoadException() {
        super("Error while loading requests.");
    }

    public RequestsLoadException(Exception cause) {
        super("Error while loading requests.", cause);
    }
}
