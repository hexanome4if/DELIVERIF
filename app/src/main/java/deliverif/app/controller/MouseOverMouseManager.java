package deliverif.app.controller;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.graphstream.graph.Edge;
import org.graphstream.ui.fx_viewer.util.FxMouseManager;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.graphicGraph.GraphicNode;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.util.InteractiveElement;


/**
 * Custom MouseManager to manage mouse events on the map.
 */ 
public class MouseOverMouseManager extends FxMouseManager {
    
    /**
     * Controller of the scene.
     */
    private final MenuPageController menuPageController;
    
    /**
     * Constructor.
     * @param of Set of element types that can respond to mouse events (Node or/and Edge or/and Sprite)
     * @param mpc Controller of the scene
     */
    public MouseOverMouseManager(EnumSet<InteractiveElement> of, MenuPageController mpc) {
        super(of);
        this.menuPageController = mpc;
    }
    
    /**
     * Initialize the mouse manager, add the listeners to the view. 
     * @param gg Graph
     * @param view View of the graph
     */
    @Override
    public void init(GraphicGraph gg, View view) {
        super.init(gg, view);
        super.release();
        view.addListener(MouseEvent.MOUSE_PRESSED, mousePressed);
    }

    /**
     * Does nothing but overrides the inherited method to
     * keep the nodes from moving.
     * @param element GraphicElement moving with the mouse
     * @param event MouseEvent 
     */
    @Override
    protected void elementMoving(GraphicElement element, MouseEvent event) {
    }
    
    /**
     * Called when an element is pressed on.
     * Update selection by calling the menuPageController method "updateSelection".
     * @param element GraphicElement Element that was clicked.
     * @param event MouseEvent
     */
    @Override
    protected void mouseButtonPressOnElement(GraphicElement element,
            MouseEvent event) {
        view.freezeElement(element, true);
        this.menuPageController.updateSelection(element);
    }

    /**
     * Handle a press on the mouse.
     * Update curElement with the element clicked if there is one
     */
    private final EventHandler<MouseEvent> mousePressed = (MouseEvent e) -> {
        curElement = view.findGraphicElementAt(getManagedTypes(), e.getX(), e.getY());
        if (curElement != null) {
            mouseButtonPressOnElement(curElement, e);
        } else {
            // Edge click
            Edge edge = selectEdge(e.getX(), e.getY());
            if (edge != null) {
                curElement = (GraphicElement) edge;
                mouseButtonPressOnElement(curElement, e);
            } else {
                x1 = e.getX();
                y1 = e.getY();
                mouseButtonPress(e);
                view.beginSelectionAt(x1, y1);
            }   
        }
    };
    
    /**
     * Finds and returns the edge on the position (px,py). 
     * @param px 
     * @param py 
     * @return The Edge on the position (px,py) or null if there is not.
     */
    private Edge selectEdge(double px, double py) {
        double ld = 5; // Max distance mouse click can be from line to be a click
        Edge se = null; // Current closest edge to mouse click that is withing max distance
        GraphicGraph gg = graph;
        List<Edge> list = graph.edges().collect(Collectors.toList());
        for (Edge ge : list) {
            // Nodes of current edge
            GraphicNode gn0 = (GraphicNode) ge.getNode0();
            GraphicNode gn1 = (GraphicNode) ge.getNode1();
            // Coordinates of node 0 and node 1
            Point3 gn0p = view.getCamera().transformGuToPx(gn0.getX(), gn0.getY(), gn0.getZ());
            Point3 gn1p = view.getCamera().transformGuToPx(gn1.getX(), gn1.getY(), gn1.getZ());
            // Values for equation of the line
            double m = (gn1p.y - gn0p.y) / (gn1p.x - gn0p.x); // slope
            double b = gn1p.y - m * gn1p.x; // y intercept
            // Distance of mouse click from the line
            double d = Math.abs(m * px - py + b) / Math.sqrt(Math.pow(m, 2) + 1);

            //System.out.println("Mouse Point: " + px + "," + py + ", GN0Point: " + gn0p.toString() + ", GN1Point: " + gn1p.toString() + ". Distance: " + d);
            // Determine lowest x (lnx), hishest x (hnx), lowest y (lny), highest y (hny)
            double lnx = gn0p.x;
            double lny = gn0p.y;
            double hnx = gn1p.x;
            double hny = gn1p.y;
            if (hnx < lnx) {
                lnx = gn1p.x;
                hnx = gn0p.x;
            }
            if (hny < lny) {
                lny = gn1p.y;
                hny = gn0p.y;
            }
            // Determine if click is close enough to line (d < ld), and click is within edge bounds (lnx <= px && lny <= py && hnx >= px && hny >= py)
            if (d < ld && lnx <= px && lny <= py && hnx >= px && hny >= py) {
                se = ge; // store edge
                ld = d; // update max distance to get the closest edge to the mouse click
            }
        }
        if (se != null) {
            return graph.getEdge(se.getId());
        }
        return null;
    }
    
    /**
     * Remove the listener from the view.
     */
    @Override
    public void release() {
        view.removeListener(MouseEvent.MOUSE_PRESSED, mousePressed);
    }
}
