/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import deliverif.app.model.map.Segment;
import deliverif.app.model.request.Path;
import java.util.ArrayList;
import java.util.Collections;
import org.graphstream.graph.Edge;

/**
 *
 * @author faouz
 */
public class SelectNodeThread extends Thread {

    private MenuPageController mpc;

    private volatile boolean continueRun = true;

    private volatile boolean isFinished = true;

    public SelectNodeThread(MenuPageController mpc) {
        this.mpc = mpc;
    }

    @Override
    public void run() {
        try {
            this.isFinished = false;
            System.out.println("start thread");
            this.mpc.setSelectedNode(null);
            while (this.mpc.getSelectedNode() == null) {}
            String pickupId = this.mpc.getSelectedNode();
            this.mpc.setSelectedNode(null);
            while (this.mpc.getSelectedNode() == null) {}
            String deliveryId = this.mpc.getSelectedNode();
            this.mpc.addRequest(pickupId, deliveryId);
            this.isFinished = true;
        } catch (Exception e) {
            System.out.println("Path SelectNodeThread crash : " + e);
        }
    }

    public void end() {
        this.continueRun = false;
    }

    public boolean isIsFinished() {
        return isFinished;
    }

}
