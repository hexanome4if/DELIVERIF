/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.graph;

import deliverif.app.model.request.Path;
import deliverif.app.model.request.PlanningRequest;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author zakaria
 */
public class Tour {
    private ArrayList<Path> paths;
    private PlanningRequest pr;
    
    public Tour (PlanningRequest pr) {
        this.pr = pr;
        this.paths = new ArrayList<Path>();
    }
    
    public float getTotalDistance() {
        float total = 0;
        for (Path p : paths) {
            total += p.getLength();
        }
        return total;
    }
    
    public int getTotalDuration () {
        return (int) (paths.get(paths.size()-1).getArrivalTime().getTime() - 
                pr.getDepot().getDepartureTime().getTime() / (60 * 1000) );
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
        return "Tour{" + "paths=" + paths + ", pr=" + pr + '}';
    }
    
    
}
