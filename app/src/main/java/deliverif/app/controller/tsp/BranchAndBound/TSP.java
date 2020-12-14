/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.tsp.BranchAndBound;

import deliverif.app.model.graph.Graph;
import deliverif.app.model.graph.Vertex;
import java.util.List;

/**
 *
 * @author zakaria
 */
public interface TSP {
    /**
     * Search for a shortest cost hamiltonian circuit in g within timeLimit milliseconds
     * (returns the best found tour whenever the time limit is reached)
     * @param timeLimit the maximum time in milliseconds the algorithm can take
     * @param g the complete graph in which to execute the algorithm
     * @param start the starting vertex
     * @param ordre the order constraint
     */
    public void searchSolution(int timeLimit, Graph g, Vertex start, List<Long> ordre);

    /**
     * Get the current best solution found by the TSP algorithm
     * @return the best tour as a list of Vertex
     */
    public Vertex[] getSolution();

    /**
     * Get the current best solution cost
     * @return current best solution cost
     */
    public float getSolutionCost();

}
