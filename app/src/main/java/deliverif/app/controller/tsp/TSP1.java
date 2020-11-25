/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.tsp;

import deliverif.app.model.graph.Graph;
import deliverif.app.model.graph.Vertex;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author zakaria
 */
public class TSP1 extends TemplateTSP {
    public float mindis;
    @Override
    protected float bound(Vertex currentVertex, Collection<Vertex> unvisited) {
        return mindis * (unvisited.size()+1) ;
    }

    @Override
    protected Iterator<Vertex> iterator(Vertex currentVertex, Collection<Vertex> unvisited, Graph g) {
            return new SeqIter(unvisited, currentVertex, g);
    }
    
    @Override
    public void searchSolution(int timeLimit, Graph g, Vertex start, List<Long> ordre){
        mindis = g.getMinDis();
        super.searchSolution(timeLimit, g, start, ordre);
    }

}
