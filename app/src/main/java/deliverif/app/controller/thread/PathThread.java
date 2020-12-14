package deliverif.app.controller.thread;

import deliverif.app.controller.MenuPageController;
import deliverif.app.model.map.Segment;
import deliverif.app.model.request.Path;
import java.util.ArrayList;
import org.graphstream.graph.Edge;

/**
 * Thread to display the selected path
 * @author H4314
 */
public class PathThread extends Thread {

    /**
     * MenuPageController instance
     */
    private MenuPageController mpc;
    
    /**
     * Position of the path in the tour
     */
    private int num;
    
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
     * @param num
     */
    public PathThread(MenuPageController mpc, int num) {
        this.mpc = mpc;
        this.num = num;
    }

    /**
     * Run the thread
     */
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
     * Sleep during 200 milliseconds
     */
    protected void sleep() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
        }
    }

    /**
     * Change the color of the path according to it uiClass, display segments on by one in blue
     * @param p path to change the color
     * @param uiClass class of the path it can be marked or unmarked
     */
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
