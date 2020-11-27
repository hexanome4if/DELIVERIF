/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.map;

import java.util.List;
import java.util.HashMap;

/**
 *
 * @author zakaria
 */
public class Map {
    private HashMap<Long, Intersection> intersections;
    private List<Segment> segments;

    public Map() {
    }

    public Map(HashMap<Long, Intersection> intersections, List<Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public HashMap<Long, Intersection> getIntersections() {
        return intersections;
    }
    
    public Intersection getIntersectionParId(Long id) {
        return intersections.get(id);
    }
    
    public List<Segment> getSegments() {
        return segments;
    }
    
    public Segment getSegmentParExtremites(Intersection origine, Intersection destination){
        for(Segment s : segments){
            if(s.getOrigin()==origine && s.getDestination()==destination){
                return s;
            }else if(s.getOrigin()==destination && s.getDestination()==origine){
                return s;
            }
        }
        return null;
    }

    public void setIntersections(HashMap<Long, Intersection> intersections) {
        this.intersections = intersections;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    @Override
    public String toString() {
        return "Map{intersections=" + intersections + ", segments=" + segments + '}';
    }
    
    
}
