package deliverif.exception;

public class MapLoadingException extends Exception {
    public MapLoadingException() {
        super("Error while loading map.");
    }

    public MapLoadingException(Exception cause) {
        super("Error while loading map.", cause);
    }
}
