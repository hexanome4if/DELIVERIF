/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.model.graph;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author zakaria
 */
public class Vertex {
    String name;        // The name of this Vertex
    List<Edge>  adj;    // The adjacency list for this Vertex
 
    int dist;      // variable for use by algorithms
    int prev;      // variable for use by algorithms
    int scratch;   // variable for use by algorithms
 
    Vertex( String nm )    {
        name = nm;                      // name of this Vertex
        adj  = new LinkedList<Edge>( ); // Start an empty adj list
    }
}
