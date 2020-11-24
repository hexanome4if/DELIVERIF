/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.tsp;

/**
 *
 * @author zakaria
 */
import deliverif.app.model.graph.Edge;
import deliverif.app.model.graph.Graph;
import deliverif.app.model.graph.Vertex;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class TemplateTSP implements TSP {
	private Vertex[] bestSol;
	protected Graph g;
	private float bestSolCost;
	private int timeLimit;
	private long startTime;
	
        
	public void searchSolution(int timeLimit, Graph g, Vertex start){
            if (timeLimit <= 0) return;
            startTime = System.currentTimeMillis();	
            this.timeLimit = timeLimit;
            this.g = g;
            bestSol = new Vertex[g.getNbVertices()];
            Collection<Vertex> unvisited = new ArrayList<>(g.getNbVertices()-1);
            for (Edge e : start.getAdj()) 
                unvisited.add(e.dest);
            Collection<Vertex> visited = new ArrayList<>(g.getNbVertices());
            visited.add(start); // The first visited vertex is 0
            bestSolCost = Integer.MAX_VALUE;
            branchAndBound(start, unvisited, visited, 0, start);
	}
	
	public Vertex[] getSolution(){
		return bestSol;
	}
	
	public float getSolutionCost(){
		if (g != null)
			return bestSolCost;
		return -1;
	}
	
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting 
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	protected abstract float bound(Vertex currentVertex, Collection<Vertex> unvisited);
	
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @param g
	 * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
	 */
	protected abstract Iterator<Vertex> iterator(Vertex currentVertex, Collection<Vertex> unvisited, Graph g);
	
	/**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 * @param currentVertex the last visited vertex
	 * @param unvisited the set of vertex that have not yet been visited
	 * @param visited the sequence of vertices that have been already visited (including currentVertex)
	 * @param currentCost the cost of the path corresponding to <code>visited</code>
	 */	
	private void branchAndBound(Vertex currentVertex, Collection<Vertex> unvisited, 
			Collection<Vertex> visited, float currentCost, Vertex start){
		if (System.currentTimeMillis() - startTime > timeLimit) return;
	    if (unvisited.size() == 0){ 
                System.out.println("Unvisited empty.");
                if(currentVertex.isEdge(start)) {
                    if (currentCost+currentVertex.getCost(start) < bestSolCost){ 
                        bestSol=visited.toArray(bestSol);
                        System.out.println("Found a solution.");
                        //System.out.println("Before visited vertices.");
                        /*for (Vertex v : bestSol){
                            System.out.println("Visited vertex: " + v);
                        }*/
                        bestSolCost = currentCost+currentVertex.getCost(start);
                    }
                }            
	    } else if (currentCost+bound(currentVertex,unvisited) < bestSolCost){
	        Iterator<Vertex> it = iterator(currentVertex, unvisited, g);
	        while (it.hasNext()){
                    Vertex nextVertex = it.next();
                    visited.add(nextVertex);
	            unvisited.remove(nextVertex);
                    //System.out.println("Removed "+ nextVertex + "from unvisited.");
	            branchAndBound(nextVertex, unvisited, visited, 
	            		currentCost+currentVertex.getCost(nextVertex), start);
	            visited.remove(nextVertex);
	            unvisited.add(nextVertex);
                    //System.out.println("Added "+ nextVertex + "to unvisited.");
	        }	    
	    }
	}
}
