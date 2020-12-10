/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.graph;

import java.util.HashMap;

/**
 *
 * @author zakaria
 */
public class Graph {

    // Maps vertex name to number
    private final HashMap<Long, Vertex> vertexMap;

    /**
     * Create a graph representing a map
     */
    public Graph() {
        vertexMap = new HashMap<>();
    }

    /**
     * Get every vertex of the graph as an hashmap
     *
     * @return an hashmap containing every vertex of the graph with their id as
     * key
     */
    public HashMap<Long, Vertex> getVertexMap() {
        return vertexMap;
    }

    /**
     * Get the number of vertices on the graph
     *
     * @return number of vertices
     */
    public Integer getNbVertices() {
        return vertexMap.size();
    }

    /**
     * Get a vertex of the graph by it's id
     *
     * @param id the vertex id to find
     * @return the vertex with this id or null
     */
    public Vertex getVertexById(Long id) {
        return vertexMap.get(id);
    }

    /**
     * Get the minimum cost in the graph
     *
     * @return the minimum distance between two vertex on the graph
     */
    public float getMinDis() {
        float min = -1;
        for (Vertex v : vertexMap.values()) {
            for (Edge e : v.getAdj()) {
                if (min == -1 || e.cost < min) {
                    min = e.cost;
                }
            }
        }
        return min;
    }

    /**
     * Add a bidirectional edge ( source, dest, cost ) to the graph.
     *
     * @param source id of the source vertex of the new edge
     * @param dest id of the destination vertex of the new edge
     * @param cost cost of the new edge
     */
    public void addEdge(Long source, Long dest, float cost) {
        Vertex srcVex = vertexMap.get(source);
        Vertex dstVex = vertexMap.get(dest);
        if (srcVex != null && dstVex != null) {
            internalAddEdge(srcVex, dstVex, cost);
        } else {
            System.out.println("source is " + srcVex + ", destination is " + dstVex);
        }
    }

    /**
     * Add an unidirectional between two vertices
     *
     * @param source id of the source vertex of the new edge
     * @param dest id of the destination vertex of the new edge
     * @param cost cost of the new edge
     */
    public void addEdgeOneSide(Long source, Long dest, float cost) {
        Vertex srcVex = vertexMap.get(source);
        Vertex dstVex = vertexMap.get(dest);
        if (srcVex != null && dstVex != null) {
            srcVex.getAdj().add(new Edge(dstVex, cost));
        } else {
            System.out.println("source is " + srcVex + ", destination is " + dstVex);
        }
    }

    /**
     * Add a vertex with the given name to the Graph, and return its internal
     * number as an Integer.This involves adding entries to the Hashtable and
     * vertex Vector. PRE: vertexName is not already in the Graph
     *
     * @param v the vertex to add
     */
    public void addVertex(Vertex v) {
        if (!vertexMap.containsKey(v.getId())) {
            vertexMap.put(v.getId(), v);
        }
    }

    /**
     * Add a bidirectional edge between two vertices
     *
     * @param source the source vertex
     * @param dest the destination vertex
     * @param cost the cost for the edge
     */
    private void internalAddEdge(Vertex source, Vertex dest, float cost) {
        source.getAdj().add(new Edge(dest, cost));
        dest.getAdj().add(new Edge(source, cost));
    }

}
