package pdtsp;

public interface RunnablePDTSP extends PDTSP, Runnable {
    /**
     * Tell the algorithm's thread to pause as soon as possible.
     * <p>
     * It means that <b>it can take some time before the thread is actually paused</b>.
     */
    void pause();

    /**
     * Resumes the algorithm's thread.
     */
    void resume();

    /**
     * Tell the algorithm's thread to stop the calculation as soon as possible.
     * <p>
     * It means that <b>it can take some time before the thread is actually killed</b>.
     */
    void kill();

    /**
     * Returns true if the algorithm's thread is ready to start the computation of a solution.
     *
     * @return true if the algorithm's thread is ready to start the computation of a solution.
     */
    boolean isReady();

    /**
     * Returns true if the algorithm's thread is alive.
     *
     * @return true if the algorithm's thread is alive.
     */
    boolean isRunning();

    /**
     * Returns true if the algorithm's thread is paused.
     *
     * @return true if the algorithm's thread is paused.
     */
    boolean isPaused();
}
