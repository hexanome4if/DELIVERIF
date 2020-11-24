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
    Long id;        // The name of this Vertex
    List<Edge>  adj;    // The adjacency list for this Vertex
 
    int dist;      // variable for use by algorithms
    int prev;      // variable for use by algorithms
    int scratch;   // variable for use by algorithms
 
    public Vertex( Long id )    {
        this.id = id;                      // name of this Vertex
        adj  = new LinkedList<Edge>( ); // Start an empty adj list
    }

    public List<Edge> getAdj() {
        return adj;
    }

    public Long getId() {
        return id;
    }
    
    public boolean isEdge(Vertex v){
        for (Edge e : adj){
            if (e.dest == v){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean equals(Object o){
        Vertex v = (Vertex) o;
        if(v.getId()==id){
            return true;
        }
        return false;
    }
    
    public float getCost(Vertex v){
        for (Edge e : adj){
            if (e.dest == v){
                return e.cost;
            }
        }
        return -1;
    }
    
    
}
