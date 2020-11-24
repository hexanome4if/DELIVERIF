/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import deliverif.app.model.graph.Edge;
import deliverif.app.model.graph.Graph;
import deliverif.app.model.graph.Vertex;
import deliverif.app.model.map.Map;
import deliverif.app.model.request.PlanningRequest;
import deliverif.app.model.request.Request;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 *
 * @author zakaria
 */
public class GraphProcessor {
    
    Graph graph;
    Map map;
    
    public GraphProcessor(Map m) {
        graph = new Graph();
        map = m;
    }
    
    public void dijkstra(Graph g, Vertex source, List<Vertex> goals){
        // Strutures
        HashMap<Long, Float> dis = new HashMap<>();  //distance : <idNoeud, distance>
        List<Vertex> gris = new ArrayList<>();          //liste des noeuds grisés (mais cet algo est sobre;)
        List<Long> noir = new ArrayList<>();         //liste des noeuds noircis

        System.out.println("source: "+source);
        System.out.println("adjacences: "+source.getAdj());
        
        // Initialisation
        for(Edge edg : source.getAdj()){
            dis.put(edg.dest.getId(),edg.cost);
            gris.add(edg.dest);
        }
        noir.add(source.getId());
        System.out.println("distance: "+dis);
        System.out.println("nb gris: "+gris.size());
        System.out.println("nb noir: "+noir.size());
        
        
        while(!gris.isEmpty() && !noir.containsAll(goals)){  //continue s'il reste des noeuds gris ou il reste des noeuds non-déterminé dans la liste goal
            float dis_MIN = Float.POSITIVE_INFINITY;
            Vertex vertex = null;
            for(Vertex v : gris){ //parcours de liste gris pour trouver le noeud dont la valeur "distance" est minimal
                if(dis.get(v.getId()) < dis_MIN){
                    dis_MIN = dis.get(v.getId());
                    vertex = v;
                }
            }
            
            for(Edge edg : vertex.getAdj()){
                if(dis.containsKey(edg.dest.getId())){
                    if(edg.cost + dis.get(vertex.getId()) < dis.get(edg.dest.getId())){ //MAJ si ce chemin est plus court
                        dis.put(edg.dest.getId(), edg.cost + dis.get(vertex.getId()));
                    } 
                } else {
                    dis.put(edg.dest.getId(), edg.cost + dis.get(vertex.getId())); //ajouter la valeur "distance" d'un noeud blanc et le griser
                    gris.add(edg.dest);
                }
            }
            
            gris.remove(vertex); //changement d'état gris->noir pour le noeud choisi
            noir.add(vertex.getId());
            
            //System.out.println("distance: "+dis);
            System.out.println("nb gris: "+gris.size());
            System.out.println("nb noir: "+noir.size());
        }
        
        for(Vertex v : goals){
            g.addEdge(source.getId(), v.getId(), dis.get(v.getId()));
        }
        
    }
    
    public Graph completeGraph(PlanningRequest pr) {
        Graph g = new Graph();
        HashMap<Long, Vertex> vertexMap = graph.getVertexMap();
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(vertexMap.get(pr.getDepot().getAddress().getId()));
        for (Request r : pr.getRequests()){
            vertices.add(vertexMap.get(r.getPickupAddress().getId()));
            vertices.add(vertexMap.get(r.getDeliveryAddress().getId()));
        }
        for (Vertex v : vertices){
            g.addVertex(v);
        }
        
        for (Vertex v : vertices){
            dijkstra(g,v,vertices);
        }
        return g;
    }
    
    public void shortestPath(Graph g, PlanningRequest pr) {
        
    }
    
    public void tsp() {
        
    }
}
