/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.model.graph;

/**
 *
 * @author zakaria
 */
public class Edge {
     // Source vertex of edge is implicit
    public int dest;  // Index of destination vertex of this edge
    public int cost;  // Cost (weight) of this edge 
 
    public Edge( int d, int c )    {
        dest = d;
        cost = c;
    }
}
