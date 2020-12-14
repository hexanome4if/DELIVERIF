/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.map;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Represent a map composed by intersections and segments
 * @author zakaria
 */
public class Map {
    /**
     * Intersections of the map
     */
    private HashMap<Long, Intersection> intersections;
    /**
     * Segments between the intersections of the map
     */
    private List<Segment> segments;

    /**
     * Create an empty map
     */
    public Map() {
    }

    /**
     * Create a new map
     *
     * @param intersections intersections of the map represented by an hashmap
     * with intersection id as key and intersection as value
     * @param segments segments between the intersections of the map
     */
    public Map(HashMap<Long, Intersection> intersections, List<Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    /**
     * Get map intersections
     *
     * @return map intersections as a hashmap with intersection id as key and
     * intersection as value
     */
    public HashMap<Long, Intersection> getIntersections() {
        return intersections;
    }

    /**
     * Get an intersection by it's id
     *
     * @param id intersection id
     * @return intersection or null
     */
    public Intersection getIntersectionParId(Long id) {
        return intersections.get(id);
    }

    /**
     * Get all the map segments
     *
     * @return map segments
     */
    public List<Segment> getSegments() {
        return segments;
    }

    /**
     * Get a segment from two intersections (the intersection order is not
     * important)
     *
     * @param origine first intersection
     * @param destination second intersection
     * @return the segment between origine and destination or null
     */
    public Segment getSegmentParExtremites(Long origine, Long destination) {
        for (Segment s : segments) {
            if (Objects.equals(s.getOrigin().getId(), origine) && Objects.equals(s.getDestination().getId(), destination)) {
                return s;
            } else if (Objects.equals(s.getOrigin().getId(), destination) && Objects.equals(s.getDestination().getId(), origine)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Set map intersections
     *
     * @param intersections map intersections
     */
    public void setIntersections(HashMap<Long, Intersection> intersections) {
        this.intersections = intersections;
    }

    /**
     * Set map segments
     *
     * @param segments map segments
     */
    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    @Override
    public String toString() {
        return "Map{intersections=" + intersections + ", segments=" + segments + '}';
    }

    /**
     * Return wether the map is empty or not
     *
     * @return wether the map is empty or not
     */
    public boolean isEmpty() {
        return intersections == null || segments == null;
    }
}
