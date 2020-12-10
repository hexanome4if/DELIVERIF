/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.graph;

import deliverif.app.controller.Observer.Observable;
import deliverif.app.model.request.Path;
import deliverif.app.model.request.PlanningRequest;
import deliverif.app.model.request.Request;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author zakaria
 */
public class Tour extends Observable {
    private ArrayList<Path> paths;
    private PlanningRequest pr;
    
    public Tour (PlanningRequest pr) {
        this.pr = pr;
        this.paths = new ArrayList<Path>();
    }
    
    public Tour (Tour t){
        this.pr = t.pr;
        paths = new ArrayList<Path>();
        for (Path p : t.getPaths()) {
            paths.add(p);
        }
    }
    
    public void copyTour (Tour t){
        this.pr = new PlanningRequest();
        pr.setDepot(t.getPr().getDepot());
        for (Request r : t.getPr().getRequests()) {
            pr.addRequest(r);
        }
        paths.clear();
        for (Path p : t.getPaths()) {
            paths.add(p);
        }
    }
    
    public float getTotalDistance() {
        float total = 0;
        for (Path p : paths) {
            total += p.getLength();
        }
        return total;
    }
    
    /**
     * 
     * @return Total duration of the tour (seconds) 
     */
    public int getTotalDuration () {
        return (int) ((paths.get(paths.size()-1).getArrivalTime().getTime() - 
                pr.getDepot().getDepartureTime().getTime() ) / 1000 );
    }
    
    public Date getDepartureTime() {
        return paths.get(0).getDepatureTime();
    }
    
    public Date getArrivalTime() {
        return paths.get(paths.size()-1).getArrivalTime();
    }
    
    public void addPaths (ArrayList<Path> pl) {
        for (Path p : pl) {
            paths.add(p);
        }
    }
    
    public void addPath (Path p) {
        paths.add(p);
    }
    
    public void removePath(Path p){
        paths.remove(p);
    }
    
    public ArrayList<Path> getPaths() {
        return paths;
    }

    public PlanningRequest getPr() {
        return pr;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }

    public void setPr(PlanningRequest pr) {
        this.pr = pr;
    }
    
    @Override
    public String toString() {
        String pathDetails = "";
        for (Path p : paths) {
            pathDetails += p.toString();
        }
        return "Tour{" + "paths=" + pathDetails + ", pr=" + pr + '}';
    }
    
    
}
