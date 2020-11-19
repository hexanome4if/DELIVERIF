/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.model.map;

/**
 *
 * @author zakaria
 */
public class Segment {
    private Intersection origin;
    private Intersection destination;
    private float length;
    private String name;

    public Segment() {
    }

    public Segment(Intersection origin, Intersection destination, float length, String name) {
        this.origin = origin;
        this.destination = destination;
        this.length = length;
        this.name = name;
    }

    public Intersection getOrigin() {
        return origin;
    }

    public Intersection getDestination() {
        return destination;
    }

    public float getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public void setOrigin(Intersection origin) {
        this.origin = origin;
    }

    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Segment{" + "origin=" + origin + ", destination=" + destination + ", length=" + length + ", name=" + name + '}';
    }
    
}
