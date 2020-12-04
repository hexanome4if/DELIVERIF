/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import deliverif.app.model.map.Segment;
import deliverif.app.model.request.Path;
import org.graphstream.graph.Edge;

/**
 *
 * @author faouz
 */
public class PathThread extends Thread{
    
    private MenuPageController mpc;
    
    public PathThread (MenuPageController mpc) {
        this.mpc = mpc;
    }
    @Override
    public void run() {
        System.out.println("computePathAction");
        
        String color = "fill-color: blue;";
        int pathIndex = 2;
        
        Path p = mpc.getTour().getPaths().get(pathIndex);
 
        for(Segment s : p.getSegments()) {
            String originId = s.getOrigin().getId().toString();
            String destId = s.getDestination().getId().toString();

            Edge edge = mpc.getGraph().getEdge(originId + "|" + destId);
            if (edge != null) {
                //edge.setAttribute("ui.style", color);
                //edge.setAttribute("ui.style", "size: 4px;");
                edge.setAttribute("ui.class", "marked");

                System.out.println("pause bleu --");
                sleep();          
                
            } else {
                edge = mpc.getGraph().getEdge(destId + "|" + originId);
                if (edge != null) {
                    edge.setAttribute("ui.class", "marked");
                    sleep(); 
                } else {
                    System.out.println("Edge not found");
                }
            }
        } 
    }
    
    protected void sleep() {
        try { Thread.sleep(500); } catch (Exception e) {}
    }
    
    
}
