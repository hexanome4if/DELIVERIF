/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.map;

/**
 *
 * @author zakaria
 */
public class Segment {

    private Intersection origin;
    private Intersection destination;
    private float length;
    private String name;

    /**
     * Create an empty segment
     */
    public Segment() {
    }

    /**
     * Create a new segment
     *
     * @param origin origin of the segment as an intersection
     * @param destination destination of the segment as an intersection
     * @param length length of the segment as a float
     * @param name segment name representing the street name
     */
    public Segment(Intersection origin, Intersection destination, float length, String name) {
        this.origin = origin;
        this.destination = destination;
        this.length = length;
        this.name = name;
    }

    /**
     * Get segment origin
     *
     * @return segment origin as an intersection
     */
    public Intersection getOrigin() {
        return origin;
    }

    /**
     * Get segment destination
     *
     * @return segment destination as an intersection
     */
    public Intersection getDestination() {
        return destination;
    }

    /**
     * Get segment length
     *
     * @return segment length
     */
    public float getLength() {
        return length;
    }

    /**
     * Get segment name
     *
     * @return segment name representing street name
     */
    public String getName() {
        return name;
    }

    /**
     * Set segment origin
     *
     * @param origin intersection representing segment origin
     */
    public void setOrigin(Intersection origin) {
        this.origin = origin;
    }

    /**
     * Set segment destination
     *
     * @param destination intersection representing segment destination
     */
    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    /**
     * Set segment length
     *
     * @param length segment length
     */
    public void setLength(float length) {
        this.length = length;
    }

    /**
     * Set segment name
     *
     * @param name segment name representing street name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Segment{" + "origin=" + origin + ", destination=" + destination + ", length=" + length + ", name=" + name + "}\n";
    }

}
