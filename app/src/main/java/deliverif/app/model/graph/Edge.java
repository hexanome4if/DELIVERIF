/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.graph;

import deliverif.app.model.graph.Vertex;

/**
 *
 * @author zakaria
 */
public class Edge {
    // Source vertex of edge is implicit
    public Vertex dest;  // Index of destination vertex of this edge
    public float cost;  // Cost (weight) of this edge 
 
    public Edge( Vertex d, float c )    {
        dest = d;
        cost = c;
    }
}
