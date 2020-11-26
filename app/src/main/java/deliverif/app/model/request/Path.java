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
    private Type type;
    private Request request;
    
    public enum Type {
        PICKUP,
        DELIVERY,
        RETURN
    }
    
    public Path () {
        
    }
    
    public Path (Path p) {
        this.segments = p.getSegments();
        this.departure = p.getDeparture();
        this.arrival = p.getArrival();
        this.length = p.getLength();
        this.depatureTime = p.getDepatureTime();
        this.arrivalTime = p.getArrivalTime();
        this.type = p.getType();
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

    public Type getType() {
        return type;
    }

    public Request getRequest() {
        return request;
    }
    
    
}
