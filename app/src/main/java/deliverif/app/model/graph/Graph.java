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

    public Graph() {
        vertexMap = new HashMap<>();
    }

    public HashMap<Long, Vertex> getVertexMap() {
        return vertexMap;
    }

    public Integer getNbVertices() {
        return vertexMap.size();
    }

    public Vertex getVertexById(Long id) {
        return vertexMap.get(id);
    }

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
     * Add the edge ( source, dest, cost ) to the graph.
     *
     * @param source
     * @param dest
     * @param cost
     */
    public void addEdge(Long source, Long dest, float cost) {
        Vertex srcVex = vertexMap.get(source);
        Vertex dstVex = vertexMap.get(dest);
        if (srcVex != null && dstVex != null) {
            internalAddEdge(srcVex, dstVex, cost);
        } else {
            //System.out.println("source is " + srcVex + ", destination is " + dstVex);
        }
    }

    public void addEdgeOneSide(Long source, Long dest, float cost) {
        Vertex srcVex = vertexMap.get(source);
        Vertex dstVex = vertexMap.get(dest);
        if (srcVex != null && dstVex != null) {
            srcVex.adj.add(new Edge(dstVex, cost));
            majVertex(srcVex);
        } else {
            //System.out.println("source is " + srcVex + ", destination is " + dstVex);
        }
    }

    /**
     * Add a vertex with the given name to the Graph, and return its internal
     * number as an Integer.This involves adding entries to the Hashtable and
     * vertex Vector. PRE: vertexName is not already in the Graph
     *
     * @param v
     */
    public void addVertex(Vertex v) {
        if (!vertexMap.containsKey(v.getId())) {
            vertexMap.put(v.getId(), v);
        }
    }

    public void majVertex(Vertex v) {
        vertexMap.put(v.getId(), v);
    }

    private void internalAddEdge(Vertex source, Vertex dest, float cost) {
        source.adj.add(new Edge(dest, cost));
        dest.adj.add(new Edge(source, cost));
        majVertex(source);
        majVertex(dest);
    }

}
