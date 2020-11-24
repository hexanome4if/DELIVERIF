/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.graph;


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

    public HashMap<Integer, Vertex> getVertexMap() {
        return vertexMap;
    }
    
    
    
    /**
    * Add the edge ( source, dest, cost ) to the graph.
     * @param source
     * @param dest
    */
    public void addEdge( Integer source, Integer dest, float cost ) {
        Vertex srcVex = vertexMap.get(source);
        Vertex dstVex = vertexMap.get(dest);
        if(srcVex != null && dstVex != null){
            internalAddEdge(srcVex,dstVex,cost);
        } else {
            System.out.println("source is " + srcVex + ", destination is " + dstVex);
        }
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
        dest.adj.add( new Edge (source, cost) );
        vertexMap.put(source.id,source);
        vertexMap.put(dest.id,dest);
    }
    
    
    /*public static void main (String[] args) {
        Graph g = new Graph();
        for(int i = 1; i<= 3000; i++){
            g.addVertex(i);
        }
        System.out.println(g.vertexMap);

        g.addEdge(1,2,7);
        g.addEdge(1,3,9);
        g.addEdge(1,6,14);
        g.addEdge(2,3,10);
        g.addEdge(3,6,2);
        g.addEdge(3,4,11);
        g.addEdge(2,4,15);
        g.addEdge(6,5,9);
        g.addEdge(4,5,6);

        for(int i = 7; i<=3000; i++){
            for(int j = 0; j<=Math.random()*6+1; j++){
                g.addEdge(i,(int)(Math.random()*3000+1),(float)(Math.random()*200+5));
            }
        }
        List<Integer> goal = new ArrayList<>();
        goal.add(5);
        goal.add(233);
        goal.add(42);
        goal.add(450);
        goal.add(666);
        goal.add(111);
        goal.add(222);
        goal.add(333);
        goal.add(444);
        goal.add(777);
        goal.add(555);
        goal.add(888);
        goal.add(999);
        goal.add(114);
        goal.add(514);
        goal.add(810);
        goal.add(110);
        goal.add(369);
        HashMap<Integer,Float> res = g.dijkstra(1,goal);
        System.out.println(res);
    }*/
}
