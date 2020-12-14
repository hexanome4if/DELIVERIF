/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.tsp.BranchAndBound;
import deliverif.app.controller.Observer.Observable;
import deliverif.app.model.graph.Edge;
import deliverif.app.model.graph.Graph;
import deliverif.app.model.graph.Vertex;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author zakaria
 */
public abstract class TemplateTSP extends Observable implements TSP {

    private Vertex[] bestSol;
    protected Graph g;
    private float bestSolCost;
    private int timeLimit;
    private long startTime;

    /**
     * Execute the algorithm to find the best tour
     * @param timeLimit the maximum time in milliseconds the algorithm can take
     * @param g the complete graph in which to execute the algorithm
     * @param start the starting vertex
     * @param ordre the order constraint
     */
    public void searchSolution(int timeLimit, Graph g, Vertex start, List<Long> ordre) {
        if (timeLimit <= 0) {
            return;
        }
        startTime = System.currentTimeMillis();
        this.timeLimit = timeLimit;
        this.g = g;
        bestSol = new Vertex[g.getNbVertices()];
        ArrayList<Vertex> unvisited = new ArrayList<>(g.getNbVertices() - 1);
        for (Edge e : start.getAdj()) {
            unvisited.add(e.dest);
        }
        List<Vertex> visited = new ArrayList<>(g.getNbVertices());
        visited.add(start); // The first visited vertex is 0
        bestSolCost = Integer.MAX_VALUE;
        branchAndBound(start, unvisited, visited, 0, start, ordre);
        this.notifiyObservers(true);
    }

    @Override
    public Vertex[] getSolution() {
        return bestSol;
    }

    @Override
    public float getSolutionCost() {
        if (g != null) {
            return bestSolCost;
        }
        return -1;
    }

    /**
     * Method that must be defined in TemplateTSP subclasses
     *
     * @param currentVertex the current vertex treated by the algorithm
     * @param unvisited list of unvisited vertexes
     * @return a lower bound of the cost of paths in g starting
     * from currentVertex, visiting every vertex in
     * unvisited exactly once, and returning back to vertex
     * 0.
     */
    protected abstract float bound(Vertex currentVertex, ArrayList<Vertex> unvisited);

    /**
     * Method that must be defined in TemplateTSP subclasses
     *
     * @param currentVertex the current vertex treated by the algorithm
     * @param unvisited list of unvisited vertexes
     * @param g the graph in which the algorithm is executed
     * @return an iterator for visiting all vertices in unvisited
     * which are successors of currentVertex
     */
    protected abstract Iterator<Vertex> iterator(Vertex currentVertex, Collection<Vertex> unvisited, Graph g);

    /**
     * Template method of a branch and bound algorithm for solving the TSP in
     * g.
     *
     * @param currentVertex the last visited vertex
     * @param unvisited the set of vertex that have not yet been visited
     * @param visited the sequence of vertices that have been already visited
     * (including currentVertex)
     * @param currentCost the cost of the path corresponding to
     * <code>visited</code>
     */
    private void branchAndBound(Vertex currentVertex, ArrayList<Vertex> unvisited,
            List<Vertex> visited, float currentCost, Vertex start,
            List<Long> ordre) {
        if (System.currentTimeMillis() - startTime > timeLimit) {
            return;
        }
        if (unvisited.isEmpty()) {
            //System.out.println("Unvisited empty.");
            if (currentVertex.isEdge(start)) {
                if (currentCost + currentVertex.getCost(start) < bestSolCost) {
                    bestSol = visited.toArray(bestSol);
                    bestSolCost = currentCost + currentVertex.getCost(start);
                    this.notifiyObservers(false);
                }
            }
        } else if (currentCost + bound(currentVertex, unvisited) < bestSolCost) {
            Iterator<Vertex> it = iterator(currentVertex, unvisited, g);

            while (it.hasNext()) {
                Vertex nextVertex = it.next();
                visited.add(nextVertex);
                unvisited.remove(nextVertex);

                boolean allowed = true;
                int index = ordre.indexOf(nextVertex.getId());
                if (index % 2 != 0) {
                    for (Vertex unv : unvisited) {
                        if (Objects.equals(unv.getId(), ordre.get(index - 1))) {
                            allowed = false;
                            break;
                        }
                    }
                }

                //System.out.println("Removed "+ nextVertex + "from unvisited.");
                if (allowed) {
                    branchAndBound(nextVertex, unvisited, visited,
                            currentCost + currentVertex.getCost(nextVertex), start, ordre);
                }
                visited.remove(nextVertex);
                unvisited.add(nextVertex);
                //System.out.println("Added "+ nextVertex + "to unvisited.");
            }
        }
    }
}
