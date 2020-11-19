/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.model.map;

import java.util.List;

/**
 *
 * @author zakaria
 */
public class Map {
    private List<Intersection> intersections;
    private List<Segment> segments;

    public Map() {
    }

    public Map(List<Intersection> intersections, List<Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    @Override
    public String toString() {
        return "Map{" + "intersections=" + intersections + ", segments=" + segments + '}';
    }
    
    
}
