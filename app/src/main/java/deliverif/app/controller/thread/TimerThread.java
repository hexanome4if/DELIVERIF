package deliverif.app.controller.thread;

import deliverif.app.controller.MenuPageController;
import java.util.Date;

/**
 * Thread that display the timer and the progress indicator when a tour is computing
 * @author H4314
 */
public class TimerThread extends Thread {

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
     * Constructor
     * @param mpc
     */
    public TimerThread(MenuPageController mpc) {
        this.mpc = mpc;
    }

    /**
     * Run the thread
     */
    @Override
    public void run() {
        try {
            mpc.getTimerPane().setVisible(true);
            mpc.getProgressIndicator().setVisible(true);
            Date beginning = new Date();
            Date current = new Date();
            int diff = 0;
            while (continueRun && !mpc.getComputeTourThread().isIsFinished()) {
                current = new Date();
                diff = (int) (current.getTime() - beginning.getTime()) / 1000;
                mpc.getTimerText().setText("Timer : " + diff + "s");
            }
           
            mpc.getTimerText().setText("Tour computed \n(" + diff + "s)");
            mpc.getProgressIndicator().setVisible(false);
            mpc.getStopResearchButton().setVisible(true);
            isFinished = true;
        } catch (Exception e) {
            System.out.println("Path Thread crash : " + e);
            isFinished = true;
        }
    }
    
    /**
     * Check if the thread execution is finished
     * @return true if the thread is finished
     */
    public boolean isIsFinished() {
        return isFinished;
    }   
    
    /**
     * Stop the thread
     */
    public void end() {
        this.continueRun = false;
    }

}
