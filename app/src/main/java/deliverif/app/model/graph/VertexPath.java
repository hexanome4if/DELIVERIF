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

    public void addVertex(Vertex v) {
        path.add(v);
    }

    public Path convertToPath(Map map) {
        Path p = new Path();
        for (int i = 0; i < path.size() - 1; ++i) {
            p.addSegment(
                    map.getSegmentParExtremites(
                            map.getIntersectionParId(path.get(i).getId()),
                            map.getIntersectionParId(path.get(i + 1).getId())
                    )
            );
        }
        return p;
    }
}
