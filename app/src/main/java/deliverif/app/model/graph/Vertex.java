/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.graph;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author zakaria
 */
public class Vertex {

    private Long id;        // The name of this Vertex
    private List<Edge> adj;    // The adjacency list for this Vertex

    private int dist;      // variable for use by algorithms
    private int prev;      // variable for use by algorithms
    private int scratch;   // variable for use by algorithms

    /**
     * Create a vertex with an id
     *
     * @param id vertex id
     */
    public Vertex(Long id) {
        this.id = id;                      // name of this Vertex
        adj = new LinkedList<>(); // Start an empty adj list
    }

    /**
     * Get the list of edges which are linked to the current vertex
     *
     * @return the list of edges
     */
    public List<Edge> getAdj() {
        return adj;
    }

    /**
     * Get vertex id
     *
     * @return vertex id
     */
    public Long getId() {
        return id;
    }

    /**
     * Check if there is an edge between the current vertex and a given vertex
     *
     * @param v the vertex to check
     * @return wether an edge exists between the current edge and v
     */
    public boolean isEdge(Vertex v) {
        for (Edge e : adj) {
            if (e.dest == v) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        Vertex v = (Vertex) o;
        return v.getId().equals(id);
    }

    /**
     * Get the cost of the path to another vertex
     *
     * @param v the other vertex
     * @return the cost to go from the current vertex to other vertex
     */
    public float getCost(Vertex v) {
        for (Edge e : adj) {
            if (e.dest == v) {
                return e.cost;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "Vertex{" + "id=" + id + '}';
    }
}
