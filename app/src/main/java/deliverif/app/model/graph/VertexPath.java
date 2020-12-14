/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.graph;

import deliverif.app.model.map.Map;
import deliverif.app.model.request.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author polo
 */
public class VertexPath {

    private final List<Vertex> path = new ArrayList<>();

    /**
     * Add a vertex to the vertex path
     *
     * @param v
     */
    public void addVertex(Vertex v) {
        path.add(v);
    }

    /**
     * Convert the vertex path to a path composed of segments
     *
     * @param map the initial map
     * @return the path composed of segments
     */
    public Path convertToPath(Map map) {
        Path p = new Path();
        for (int i = 0; i < path.size()-1; i++) {
            p.addSegment(
                map.getSegmentParExtremites(path.get(i).getId(),path.get(i+1).getId())
            );
            p.setDeparture(map.getIntersectionParId(path.get(0).getId()));
            p.setArrival(map.getIntersectionParId(path.get(path.size()-1).getId()));
        }
        return p;
    }

    public List<Vertex> getPath() {
        return path;
    }
    
    
}
