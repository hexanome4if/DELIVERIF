/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.thread;

import deliverif.app.controller.MenuPageController;
import deliverif.app.model.request.Path;
import java.util.Date;

/**
 *
 * @author faouz
 */
public class TimerThread extends Thread {

    private MenuPageController mpc;

    private volatile boolean continueRun = true;
    
    private volatile boolean isFinished = false;

    public TimerThread(MenuPageController mpc) {
        this.mpc = mpc;
    }

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
            mpc.getRenderTourButton().setVisible(true);
            isFinished = true;
        } catch (Exception e) {
            System.out.println("Path Thread crash : " + e);
            isFinished = true;
        }
    }
    
    public boolean isIsFinished() {
        return isFinished;
    }   
    
    public void end() {
        this.continueRun = false;
    }

}
