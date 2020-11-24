/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.view;

import java.util.EnumSet;
import javafx.scene.input.MouseEvent;
import org.graphstream.ui.fx_viewer.util.FxMouseManager;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.util.InteractiveElement;

/**
 *
 * @author faouz
 */
public class MouseOverMouseManager extends FxMouseManager {

    public MouseOverMouseManager(EnumSet<InteractiveElement> of) {
        super(of);
    }

    @Override
    public void init(GraphicGraph gg, View view) {
        System.out.println("init"); //To change body of generated methods, choose Tools | Templates.
        super.init(gg, view);
    }

    @Override
    public void release() {
        super.release();
    }

    @Override
    public EnumSet<InteractiveElement> getManagedTypes() {
        return super.getManagedTypes();
    }

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
                float value = 1;
                element.setAttribute("ui.color", value);
            }
        }
    }

    protected void mouseButtonPress(MouseEvent event) {
        view.requireFocus();
        float value = 0;
        float val = (float) 0.5;
        // Unselect all.
        if (!event.isShiftDown()) {
            graph.nodes().filter(n -> n.hasAttribute("ui.selected")).forEach(n -> {n.removeAttribute("ui.selected"); n.setAttribute("ui.color", value);});
            graph.sprites().filter(s -> s.hasAttribute("ui.selected")).forEach(s -> s.removeAttribute("ui.selected"));
            graph.edges().filter(e -> e.hasAttribute("ui.selected")).forEach(e -> {e.removeAttribute("ui.selected"); e.setAttribute("ui.color", val);});
        }
    }
}
