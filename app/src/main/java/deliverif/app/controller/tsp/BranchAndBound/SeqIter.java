/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.tsp.BranchAndBound;

import deliverif.app.model.graph.Graph;
import deliverif.app.model.graph.Vertex;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author zakaria
 */
public class SeqIter implements Iterator<Vertex> {

    private Vertex[] candidates;
    private int nbCandidates = 0;

    /**
     * Create an iterator to traverse the set of vertices in
     * unvisited which are successors of currentVertex
     * in g Vertices are traversed in the same order as in
     * unvisited
     *
     * @param unvisited list of unvisited vertexes
     * @param currentVertex current vertex treated by the algorithm
     * @param g the graph used by the algorithm
     */
    public SeqIter(Collection<Vertex> unvisited, Vertex currentVertex, Graph g) {
        this.candidates = new Vertex[unvisited.size()];
        for (Vertex s : unvisited) {
            if (currentVertex.isEdge(s)) {
                candidates[nbCandidates++] = s;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return nbCandidates > 0;
    }

    @Override
    public Vertex next() {
        nbCandidates--;
        return candidates[nbCandidates];
    }

    @Override
    public void remove() {
    }

}
