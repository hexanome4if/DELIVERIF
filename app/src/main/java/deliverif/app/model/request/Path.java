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
 * Represents a path of segments between two points on the map
 * @author zakaria
 */
public class Path {
    /**
     * List of segments to go from departure to arrival
     */
    private ArrayList<Segment> segments;
    /**
     * Departure point
     */
    private Intersection departure;
    /**
     * Arrival point
     */
    private Intersection arrival;
    /**
     * Length of the path
     */
    private float length;
    /**
     * Departure time from departure point
     */
    private Date depatureTime;
    /**
     * Arrival time to arrival point
     */
    private Date arrivalTime;
    /**
     * THe request containing the departure point
     */
    private Request request;
    /**
     * The type of the path
     */
    private Type type;

    public enum Type {
        PICKUP,
        DELIVERY,
        WAREHOUSE
    }

    /**
     * Create an empty path
     */
    public Path() {
        this.segments = new ArrayList<>();
        this.length = 0;
    }

    /**
     * Create a copy of a path
     *
     * @param p the path to copy informations from
     */
    public Path(Path p) {
        this.segments = p.getSegments();
        this.departure = p.getDeparture();
        this.arrival = p.getArrival();
        this.length = p.getLength();
        this.depatureTime = p.getDepatureTime();
        this.arrivalTime = p.getArrivalTime();
        this.request = p.getRequest();
        this.type = p.getType();
    }
   

    
    /**
     * Get path's segments
     *
     * @return path's segments
     */
    public ArrayList<Segment> getSegments() {
        return segments;
    }

    /**
     * Get path departure point
     *
     * @return intersection representing departure point
     */
    public Intersection getDeparture() {
        return departure;
    }

    /**
     * Get path arrival
     *
     * @return intersection representing arrival point
     */
    public Intersection getArrival() {
        return arrival;
    }

    /**
     * Get path length
     *
     * @return path length
     */
    public float getLength() {
        return length;
    }

    /**
     * Get departure time
     *
     * @return departure time
     */
    public Date getDepatureTime() {
        return depatureTime;
    }

    /**
     * Get arrival time
     *
     * @return arrival time
     */
    public Date getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Get the request represented by the path
     *
     * @return requests of the path
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Get path type
     *
     * @return path type
     */
    public Type getType() {
        return type;
    }

    /**
     * Set path's segments
     *
     * @param segments segments representing the path
     */
    public void setSegments(ArrayList<Segment> segments) {
        this.segments = segments;
    }

    /**
     * Set path departure
     *
     * @param departure intersection representing the departure point
     */
    public void setDeparture(Intersection departure) {
        this.departure = departure;
    }

    /**
     * Set path arrival
     *
     * @param arrival intersection representing the arrival point
     */
    public void setArrival(Intersection arrival) {
        this.arrival = arrival;
    }

    /**
     * Set path length
     *
     * @param length path length
     */
    public void setLength(float length) {
        this.length = length;
    }

    /**
     * Set departure time
     *
     * @param depatureTime departure time as a date object
     */
    public void setDepatureTime(Date depatureTime) {
        this.depatureTime = depatureTime;
    }

    /**
     * Set arrival time
     *
     * @param arrivalTime arrival time as a date object
     */
    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * Set the request representing the path
     *
     * @param request request represented by the path
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * Add a segment to the path
     *
     * @param s the segemnt to add
     */
    public void addSegment(Segment s) {
        if (segments.isEmpty()) {
            departure = s.getOrigin();
        }
        this.segments.add(s);
        this.arrival = s.getDestination();
        this.length += s.getLength();
    }

    @Override
    public String toString() {
        return "Departure: " + departure + "\nArrival: " + arrival + "\nLength: " + length;
    }

}
