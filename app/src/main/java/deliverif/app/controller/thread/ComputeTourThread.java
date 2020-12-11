/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.thread;

import deliverif.app.controller.MenuPageController;
import deliverif.app.model.graph.Tour;

/**
 *
 * @author faouz
 */
public class ComputeTourThread extends Thread {

    private MenuPageController mpc;
    private volatile boolean continueRun = true;
    private volatile boolean isFinished = false;
    private Tour tour = null;

    public ComputeTourThread(MenuPageController mpc) {
        this.mpc = mpc;
    }

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

    public void end() {
        this.continueRun = false;
    }

    public boolean isIsFinished() {
        return isFinished;
    }

    public Tour getTour() {
        return tour;
    }
}
