/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.tsp.BranchAndBound;

import deliverif.app.model.graph.Edge;
import deliverif.app.model.graph.Graph;
import deliverif.app.model.graph.Vertex;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author zakaria
 */
public class TSP1 extends TemplateTSP {

    public float mindis;
    public HashMap<String, Float> costs = new HashMap<>();

    @Override
    protected float bound(Vertex currentVertex, ArrayList<Vertex> unvisited) {
        float total1 = Float.MAX_VALUE;
        float total2 = 0;
        for (int i = 0; i < unvisited.size(); i++) {
            float current = costs.get(currentVertex.getId() + "-" + unvisited.get(i).getId());
            if (current < total1) {
                total1 = current;
            }
            Long current2 = unvisited.get(i).getId();
            float shortest = costs.get(current2 + "-" + "0");
            for (int j = 0; j < unvisited.size(); j++) {
                if (i != j) {
                    float curr = costs.get(current2 + "-" + unvisited.get(j).getId());
                    if (curr < shortest) {
                        shortest = curr;
                    }
                }
            }
            total2 += shortest;
        }
        return total1 + total2;
    }

    // @Override
    protected float bound2(Vertex currentVertex, ArrayList<Vertex> unvisited) {
        return mindis * (unvisited.size() + 1);
    }

    protected void onInit(Graph g, Vertex start) {
        for (Vertex v : g.getVertexMap().values()) {
            for (Edge e : v.getAdj()) {
                if (e.dest.getId().equals(start.getId())) {
                    costs.put(v.getId() + "-" + "0", e.cost);
                } else {
                    costs.put(v.getId() + "-" + e.dest.getId(), e.cost);
                }
            }
        }
    }

    @Override
    protected Iterator<Vertex> iterator(Vertex currentVertex, Collection<Vertex> unvisited, Graph g) {
        return new SeqIter(unvisited, currentVertex, g);
    }

    @Override
    public void searchSolution(int timeLimit, Graph g, Vertex start, List<Long> ordre) {
        onInit(g, start);
        // mindis = g.getMinDis();
        super.searchSolution(timeLimit, g, start, ordre);
    }

}
