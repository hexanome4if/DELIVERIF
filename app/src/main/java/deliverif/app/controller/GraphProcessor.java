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
    
    public HashMap<Integer,Float> dijkstra(Integer sourceId, List<Integer> goals){
        // Strutures
        HashMap<Integer, Float> dis = new HashMap<>();  //distance : <idNoeud, distance>
        List<Vertex> gris = new ArrayList<>();          //liste des noeuds grisés (mais cet algo est sobre;)
        List<Integer> noir = new ArrayList<>();         //liste des noeuds noircis
        Vertex source = graph.getVertexMap().get(sourceId);
        System.out.println("source: "+source);
        System.out.println("adjacences: "+source.getAdj());
        
        // Initialisation
        for(Edge edg : source.getAdj()){
            dis.put(edg.dest.getId(),edg.cost);
            gris.add(edg.dest);
        }
        noir.add(sourceId);
        System.out.println("distance: "+dis);
        System.out.println("nb gris: "+gris.size());
        System.out.println("nb noir: "+noir.size());
        
        
        while(!gris.isEmpty() && !noir.containsAll(goals)){  //continue s'il reste des noeuds gris ou il reste des noeuds non-déterminé dans la liste goal
            float dis_MIN = Float.POSITIVE_INFINITY;
            int noeudId = 0;
            for(Vertex v : gris){ //parcours de liste gris pour trouver le noeud dont la valeur "distance" est minimal
                if(dis.get(v.getId()) < dis_MIN){
                    dis_MIN = dis.get(v.getId());
                    noeudId = v.getId();
                }
            }
            Vertex vertex = graph.getVertexMap().get(noeudId); 
            
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
        HashMap<Integer,Float> result = new HashMap<>();
        for(Integer i : goals){
            result.put(i,dis.get(i));
        }
        return result;
    }
    
    public Graph completeGraph(PlanningRequest pr) {
        Graph g = new Graph();
    }
    
    public void shortestPath(Graph g, PlanningRequest pr) {
        
    }
    
    public void tsp() {
        
    }
}
