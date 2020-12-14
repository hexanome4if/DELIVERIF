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
import java.util.Calendar;
import java.util.Date;

/**
 * Represents a tour between different points of the map
 * @author zakaria
 */
public class Tour extends Observable {
    /**
     * List of paths to go from a point to the next one
     */
    private ArrayList<Path> paths;
    /**
     * Planning request represented by the Tour
     */
    private PlanningRequest pr;

    /**
     * Create a new tour
     *
     * @param pr the planning request of the tour
     */
    public Tour(PlanningRequest pr) {
        this.pr = pr;
        this.paths = new ArrayList<>();
    }

    /**
     * Create a copy of tour
     *
     * @param t the tour to copy
     */
    public Tour(Tour t) {
        this.pr = t.pr;
        paths = new ArrayList<>();
        for (Path p : t.getPaths()) {
            paths.add(p);
        }
    }
    
    /**
     * update the timestamps of {@code Path} using the data of the planning request
     */
    public void update(){
        ArrayList<Path> newPaths = new ArrayList<>();
        double velocity = 15 * 1000 / 60;
        for(Path p : paths){
            if(paths.indexOf(p) == 0){
                Calendar cal = Calendar.getInstance();
                cal.setTime(pr.getDepot().getDepartureTime());
                p.setDepatureTime(cal.getTime());
                double commute = p.getLength() / velocity;
                cal.add(Calendar.MINUTE, (int) commute);
                p.setArrivalTime(cal.getTime());
                p.setRequest(pr.findRequestByAddress(p.getArrival().getId()));
            }
            else {
                //MAJ departureTime = last arraivalTime + its duration
                Calendar cal = Calendar.getInstance();
                cal.setTime(paths.get(paths.indexOf(p)-1).getArrivalTime());
                cal.add(Calendar.SECOND, pr.getDuration(p.getDeparture().getId()));
                p.setDepatureTime(cal.getTime());
                
                //MAJ arrivalTime = departureTime + commuteTime
                double commute = p.getLength() / velocity;
                cal.add(Calendar.MINUTE, (int) commute);
                p.setArrivalTime(cal.getTime());
                
                //p.request = request corresponding to the destination of this path
                if(paths.indexOf(p) == paths.size()-1){
                    p.setRequest(pr.findRequestByAddress(p.getArrival().getId()));
                }
            }
            newPaths.add(p);
        }
        this.setPaths(newPaths);
    }
    
    /**
     * Copy the content of a given tour in the current tour
     *
     * @param t the tour to copy
     */
    public void copyTour(Tour t) {
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

    /**
     * Get total tour distance
     *
     * @return total tour distance
     */
    public float getTotalDistance() {
        float total = 0;
        for (Path p : paths) {
            total += p.getLength();
        }
        return total;
    }
    
    public ArrayList<Long> getOrder(){
        ArrayList<Long> order = new ArrayList<>();
        order.add(paths.get(0).getDeparture().getId());
        for(Path p : paths){
            order.add(p.getArrival().getId());
        }
        return order;
    }
      
    /**
     * Get total tour duration
     *
     * @return Total duration of the tour (seconds)
     */
    public int getTotalDuration() {
        return (int) ((paths.get(paths.size() - 1).getArrivalTime().getTime()
                - pr.getDepot().getDepartureTime().getTime()) / 1000);
    }

    /**
     * Get tour departure time
     *
     * @return tour departure time
     */
    public Date getDepartureTime() {
        return paths.get(0).getDepatureTime();
    }

    /**
     * Get tour arrival time
     *
     * @return tour arrival time
     */
    public Date getArrivalTime() {
        return paths.get(paths.size() - 1).getArrivalTime();
    }

    /**
     * Add a list of paths to the tour
     *
     * @param pl the list of paths to add
     */
    public void addPaths(ArrayList<Path> pl) {
        for (Path p : pl) {
            paths.add(p);
        }
    }

    /**
     * Add a path to the tour
     *
     * @param p the path to add
     */
    public void addPath(Path p) {
        paths.add(p);
    }

    /**
     * Remove a path from the tour
     *
     * @param p the path to remove
     */
    public void removePath(Path p) {
        paths.remove(p);
    }

    /**
     * Get the tour list of paths
     *
     * @return the list of paths
     */
    public ArrayList<Path> getPaths() {
        return paths;
    }

    /**
     * Get the tour planning request
     *
     * @return the tour planning request
     */
    public PlanningRequest getPr() {
        return pr;
    }

    /**
     * Set the tour list of paths
     *
     * @param paths list of paths
     */
    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }

    /**
     * Set the planning request linked to the tour
     *
     * @param pr the new planning request
     */
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
