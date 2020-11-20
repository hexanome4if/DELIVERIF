/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.model.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author zakaria
 */
public class Graph {
    // Maps vertex name to number
    private HashMap<Integer, Vertex> vertexMap;
     
 
    private static final int NULL_VERTEX = -1;
 
    public Graph()    {
        vertexMap   = new HashMap<Integer, Vertex>();
    }
    /**
    * Add the edge ( source, dest, cost ) to the graph.
     * @param source
     * @param dest
    */
    public void addEdge( Integer source, Integer dest, float cost ) {
        Vertex sourceVec = vertexMap.get(source);
        Vertex destVec = vertexMap.get(dest);
        internalAddEdge( sourceVec, destVec, cost );
    }
    /**
    * Add a vertex with the given name to the Graph, and
    * return its internal number as an Integer.
    * This involves adding entries to the Hashtable and vertex Vector.
    * PRE: vertexName is not already in the Graph
    */
    private Integer addVertex( Integer id ) {
        Vertex v = new Vertex(id);
        vertexMap.put(id,v);
        return id;
    }
    

    private void internalAddEdge(Vertex source, Vertex dest, float cost){
        source.adj.add( new Edge (dest, cost) );
    }
}
