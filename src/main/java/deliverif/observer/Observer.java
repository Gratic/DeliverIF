package deliverif.observer;

/**
 * From Christine Solnon's PlaCo application source code
 */
public interface Observer {
    void update(Observable observed, Object arg);
}
