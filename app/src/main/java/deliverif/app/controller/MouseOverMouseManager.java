/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import java.awt.Component;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.EnumSet;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.util.FxMouseManager;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.graphicGraph.GraphicNode;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.camera.Camera;
import org.graphstream.ui.view.util.InteractiveElement;

/**
 *
 * @author faouz
 */
public class MouseOverMouseManager extends FxMouseManager {

    /*
        Construction
     */
    public MouseOverMouseManager(EnumSet<InteractiveElement> of) {
        super(of);
    }

    @Override
    public void init(GraphicGraph gg, View view) {
        System.out.println("init"); //To change body of generated methods, choose Tools | Templates.
        super.init(gg, view);
        super.release();
        view.addListener(MouseEvent.MOUSE_PRESSED, mousePressed);
        view.addListener(MouseEvent.MOUSE_DRAGGED, mouseDragged);
        view.addListener(MouseEvent.MOUSE_RELEASED, mouseRelease);
    }

    /*
        Command
     */
    @Override
    protected void elementMoving(GraphicElement element, MouseEvent event) {
        //Empecher les noeuds de bouger 
        //view.moveElementAtPx(element, event.getX(), event.getY());
        //System.out.println("elementMoving");
    }

    @Override
    protected void mouseButtonRelease(MouseEvent event,
            Iterable<GraphicElement> elementsInArea) {
        for (GraphicElement element : elementsInArea) {
            if (!element.hasAttribute("ui.selected")) {
                element.setAttribute("ui.selected");
            }
        }
    }

    @Override
    protected void mouseButtonPressOnElement(GraphicElement element,
            MouseEvent event) {
        view.freezeElement(element, true);
        if (event.getButton() == MouseButton.SECONDARY) {
            element.setAttribute("ui.selected");
        } else {
            element.setAttribute("ui.clicked");
        }
        System.out.println("Press on " + element.getSelectorType() + " id=" + element.getId());
    }

    @Override
    protected void mouseButtonPress(MouseEvent event) {
        view.requireFocus();
        float value = 0;
        float val = (float) 0.5;
        // Unselect all.
        if (!event.isShiftDown()) {
            graph.nodes().filter(n -> n.hasAttribute("ui.selected")).forEach(n -> {
                n.removeAttribute("ui.selected");
                n.setAttribute("ui.color", value);
            });
            graph.sprites().filter(s -> s.hasAttribute("ui.selected")).forEach(s -> s.removeAttribute("ui.selected"));
            graph.edges().filter(e -> e.hasAttribute("ui.selected")).forEach(e -> {
                e.removeAttribute("ui.selected");
                e.setAttribute("ui.color", val);
            });
        }
    }

    /*
        Mouse Listener
     */
    EventHandler<MouseEvent> mouseRelease = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (curElement != null) {
                mouseButtonReleaseOffElement(curElement, event);
                curElement = null;
            } else {
                double x2 = event.getX();
                double y2 = event.getY();
                double t;

                if (x1 > x2) {
                    t = x1;
                    x1 = x2;
                    x2 = t;
                }
                if (y1 > y2) {
                    t = y1;
                    y1 = y2;
                    y2 = t;
                }
                view.endSelectionAt(x2, y2);
                //view.getCamera().setViewCenter((x1+x2)/2, (y1+y2)/2, 0);
                view.getCamera().setViewPercent(0.50);
            }
        }
    };

    EventHandler<MouseEvent> mousePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            curElement = view.findGraphicElementAt(getManagedTypes(), e.getX(), e.getY());
            if (curElement != null) {
                mouseButtonPressOnElement(curElement, e);
            } else {
                /*
                    Reconnaissance des segments
                
                Edge edge = selectEdge(e.getX(), e.getY());
                if (edge != null) {
                    curElement = (GraphicElement) edge;
                    mouseButtonPressOnElement(curElement, e);
                } else { */
                x1 = e.getX();
                y1 = e.getY();
                mouseButtonPress(e);
                view.beginSelectionAt(x1, y1);

                
            }
        }
    };

    EventHandler<MouseEvent> mouseDragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (curElement != null) {
                elementMoving(curElement, event);
            } else {
                view.selectionGrowsAt(event.getX(), event.getY());
            }
        }
    };

    private Edge selectEdge(double px, double py) {
        double ld = 5; // Max distance mouse click can be from line to be a click
        Edge se = null; // Current closest edge to mouse click that is withing max distance
        GraphicGraph gg = graph;
        for (int i = 0; i < gg.getEdgeCount(); i++) {
            Edge ge = gg.getEdge(i);
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

            System.out.println("Mouse Point: " + px + "," + py + ", GN0Point: " + gn0p.toString() + ", GN1Point: " + gn1p.toString() + ". Distance: " + d);

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
            System.out.println("Selected edge: " + se.getId());
            return graph.getEdge(se.getId());
        }
        return null;
    }

    @Override
    public void release() {
        view.removeListener(MouseEvent.MOUSE_PRESSED, mousePressed);
        view.removeListener(MouseEvent.MOUSE_DRAGGED, mouseDragged);
        view.removeListener(MouseEvent.MOUSE_RELEASED, mouseRelease);
    }

    @Override
    public EnumSet<InteractiveElement> getManagedTypes() {
        return super.getManagedTypes();
    }

}
