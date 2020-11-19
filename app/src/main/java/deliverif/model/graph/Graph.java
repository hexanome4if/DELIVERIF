/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.model.graph;

import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author zakaria
 */
public class Graph {
    // Maps vertex name to number
    private HashMap<String,Integer> vertexMap;
    
    // Vector of Vertices in Graph
 
    private Vector<Vertex> vertexVec;   
 
    private static final int NULL_VERTEX = -1;
 
    public Graph()    {
        vertexVec   = new Vector<Vertex>();
        vertexMap   = new HashMap<String,Integer>();
    }
    /**
    * Add the edge ( source, dest, cost ) to the graph.
    */
    public void addEdge( String source, String dest, int cost ) {
    // get internal index of source, creating it if necessary
    Integer sourceNum = vertexMap.get(source);
    if (sourceNum == null) // new vertex, not seen before; add it
            sourceNum = addVertex(source);

    // get internal index of dest, creating it if necessary
    Integer destNum = vertexMap.get(dest);
    if (destNum == null) // new vertex, not seen before; add it
            destNum = addVertex(dest);

    // now actually add the edge to the graph
       internalAddEdge( sourceNum, destNum, cost );
    }
    /**
    * Add a vertex with the given name to the Graph, and
    * return its internal number as an Integer.
    * This involves adding entries to the Hashtable and vertex Vector.
    * PRE: vertexName is not already in the Graph
    */
    private int addVertex( String vertexName ) {
        // get the next unused index; this will be the internal number
            int indx = vertexVec.size();

        // create a new Vertex, put it at end of vertexVec,
        // where it will have index indx
            vertexVec.add( new Vertex(vertexName) );

        // associate the vertex name and index in the vertexMap
            vertexMap.put( vertexName, indx );

        // return the internal vertex number
            return indx;
    }
    
    /**
    * Add an edge given internal index numbers of its vertices.
    * PRE: source and dest are indexes of Vertexes in vertexVec
    */
    private void internalAddEdge(int source, int dest, int cost)
    {
    // get the source Vertex, and add an edge to dest Vertex
    // to its adjacency list
            vertexVec.get( source ).adj.add( new Edge (dest, cost) );
    }
}
