package deliverif.exception;

public class DeliverifException extends Exception {

    public DeliverifException(String message) {
        super(message);
    }

    public DeliverifException(String message, Throwable cause){
        super(message, cause);
    }

}
