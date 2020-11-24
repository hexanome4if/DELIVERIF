/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.tsp;

import deliverif.app.model.graph.Graph;
import deliverif.app.model.graph.Vertex;

/**
 *
 * @author zakaria
 */
public interface TSP {
    /**
     * Search for a shortest cost hamiltonian circuit in <code>g</code> within <code>timeLimit</code> milliseconds
     * (returns the best found tour whenever the time limit is reached)
     * Warning: The computed tour always start from vertex 0
     * @param limitTime
     * @param g
     */
    public void searchSolution(int timeLimit, Graph g, Vertex start);

    /**
     * @param i
     * @return the ith visited vertex in the solution computed by <code>searchSolution</code> 
     * (-1 if <code>searcheSolution</code> has not been called yet, or if i < 0 or i >= g.getNbSommets())
     */
    public Vertex[] getSolution();

    /** 
     * @return the total cost of the solution computed by <code>searchSolution</code> 
     * (-1 if <code>searcheSolution</code> has not been called yet).
     */
    public float getSolutionCost();

}
