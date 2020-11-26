/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.request;

import deliverif.app.model.map.Intersection;
import deliverif.app.model.map.Segment;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author zakaria
 */
public class Path {
    private ArrayList<Segment> segments;
    private Intersection departure;
    private Intersection arrival;
    private float length;
    private Date depatureTime;
    private Date arrivalTime;
    private Request request;
    
    
    public Path () {
        
    }
    
    public Path (Path p) {
        this.segments = p.getSegments();
        this.departure = p.getDeparture();
        this.arrival = p.getArrival();
        this.length = p.getLength();
        this.depatureTime = p.getDepatureTime();
        this.arrivalTime = p.getArrivalTime();
        this.request = p.getRequest();
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public Intersection getDeparture() {
        return departure;
    }

    public Intersection getArrival() {
        return arrival;
    }

    public float getLength() {
        return length;
    }

    public Date getDepatureTime() {
        return depatureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public Request getRequest() {
        return request;
    }

    public void setSegments(ArrayList<Segment> segments) {
        this.segments = segments;
    }

    public void setDeparture(Intersection departure) {
        this.departure = departure;
    }

    public void setArrival(Intersection arrival) {
        this.arrival = arrival;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void setDepatureTime(Date depatureTime) {
        this.depatureTime = depatureTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
    
    public void addSegment (Segment s) {
        this.segments.add(s);
        this.arrival = s.getDestination();
        this.length += s.getLength();
    }
    
}
