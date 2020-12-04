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
public class PathThread extends Thread {

    private MenuPageController mpc;

    private int num;

    private volatile boolean continueRun = true;

    private volatile boolean isFinished = true;

    public PathThread(MenuPageController mpc, int num) {
        this.mpc = mpc;
        this.num = num;
    }

    @Override
    public void run() {
        try {
            this.isFinished = false;
            System.out.println("computePathAction");

            String color = "fill-color: blue;";
            int pathIndex = num - 1;

            Path p = mpc.getTour().getPaths().get(pathIndex);
            while (this.continueRun) {
                this.changeColor(p, "marked");
                sleep();
                this.changeColor(p, "unmarked");
            }
            this.isFinished = true;
        } catch (Exception e) {
            System.out.println("Path Thread crash : " + e);
        }
    }

    public void end() {
        this.continueRun = false;
    }

    public boolean isIsFinished() {
        return isFinished;
    }

    protected void sleep() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
        }
    }

    private void changeColor(Path p, String uiClass) {
        try {
            ArrayList<Segment> listSegment = p.getSegments();
            for (int i = listSegment.size() - 1; i >= 0; i--) {
                if (!this.continueRun && uiClass.equals("marked")) {
                    break;
                }
                Segment s = listSegment.get(i);
                String originId = s.getOrigin().getId().toString();
                String destId = s.getDestination().getId().toString();

                Edge edge = mpc.getGraph().getEdge(originId + "|" + destId);
                if (edge != null) {
                    edge.setAttribute("ui.class", uiClass);
                    if (uiClass.equals("marked")) {
                        sleep();
                    }
                } else {
                    edge = mpc.getGraph().getEdge(destId + "|" + originId);
                    if (edge != null) {
                        edge.setAttribute("ui.class", uiClass);
                        if (uiClass.equals("marked")) {
                            sleep();
                        }
                    } else {
                        System.out.println("Edge not found");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in changeColor :" + e);
        }

    }

}
