package pdtsp;

public interface RunnablePDTSP extends PDTSP, Runnable {
    void pause();

    void resume();

    void kill();

    boolean isReady();

    boolean isRunning();

    boolean isPaused();
}
