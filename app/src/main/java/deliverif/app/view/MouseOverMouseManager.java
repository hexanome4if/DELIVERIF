/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.view;

import java.util.EnumSet;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.util.InteractiveElement;
import org.graphstream.ui.view.util.MouseManager;

/**
 *
 * @author fabien
 */
public class MouseOverMouseManager implements MouseManager {

    public MouseOverMouseManager(EnumSet<InteractiveElement> of) {
    }

    @Override
    public void init(GraphicGraph gg, View view) {
        System.out.println("test1");
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void release() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("test2");
    }

    @Override
    public EnumSet<InteractiveElement> getManagedTypes() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("test3");
        return null;
    }
    
}
