package deliverif.app.controller.thread;

import deliverif.app.controller.MenuPageController;
import deliverif.app.model.graph.Tour;

/**
 * Thread to compute a tour
 * @author H4314
 */
public class ComputeTourThread extends Thread {

    /**
     * MenuPageController instance
     */
    private MenuPageController mpc;
    
    /**
     * Boolean to check if the thread must continue to run or stop
     */
    private volatile boolean continueRun = true;
    /**
     * Boolean to check if the thread execution is finished
     */
    private volatile boolean isFinished = false;
    /**
     * Current tour
     */
    private Tour tour = null;

    /**
     * Constructor
     * @param mpc
     */
    public ComputeTourThread(MenuPageController mpc) {
        this.mpc = mpc;
    }

    /**
     * Run the thread
     */
    @Override
    public void run() {
        try {
            mpc.getGraphProcessor().startAlgo();
            isFinished = true;
        } catch (Exception e) {
            System.err.println("ComputeTourThread error : " + e);
            isFinished = true;
        }
    }

    /**
     * Stop the thread
     */
    public void end() {
        this.continueRun = false;
    }

    /**
     * Check if the thread execution is finished
     * @return true if the thread is finished
     */
    public boolean isIsFinished() {
        return isFinished;
    }

    /**
     * Get the current tour
     * @return tour
     */
    public Tour getTour() {
        return tour;
    }
}
