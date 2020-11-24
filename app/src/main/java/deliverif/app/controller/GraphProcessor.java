/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import deliverif.app.controller.tsp.TSP1;
import deliverif.app.model.graph.Edge;
import deliverif.app.model.graph.Graph;
import deliverif.app.model.graph.Vertex;
import deliverif.app.model.map.Intersection;
import deliverif.app.model.map.Map;
import deliverif.app.model.map.Segment;
import deliverif.app.model.request.PlanningRequest;
import deliverif.app.model.request.Request;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;



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
        for (Intersection i : m.getIntersections().values()){
            graph.addVertex(new Vertex(i.getId()));
        }
        for (Segment s : m.getSegments()){
            graph.addEdge(s.getOrigin().getId(), s.getDestination().getId(), s.getLength());
        }
    }
    
    public void dijkstra(Graph g, Vertex source, List<Vertex> goals){
        // Strutures
        HashMap<Long, Float> dis = new HashMap<>();  //distance : <idNoeud, distance>
        List<Vertex> gris = new ArrayList<>();          //liste des noeuds grisés (mais cet algo est sobre;)
        List<Vertex> noir = new ArrayList<>();         //liste des noeuds noircis

        //System.out.println("source: "+source);
        //System.out.println("adjacences: "+source.getAdj());
        
        // Initialisation
        for(Edge edg : source.getAdj()){
            dis.put(edg.dest.getId(),edg.cost);
            gris.add(edg.dest);
        }
        noir.add(source);
        //System.out.println("distance: "+dis);
        //System.out.println("nb gris: "+gris.size());
        //System.out.println("nb noir: "+noir.size());
        
        
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
            noir.add(vertex);
            
            //System.out.println("distance: "+dis);
            //System.out.println("nb gris: "+gris.size());
            //System.out.println("nb noir: "+noir.size());
        }
        for(Vertex v : goals){
            if(Objects.equals(v.getId(), source.getId())) {
            } else {
                g.addEdge(source.getId(), v.getId(), dis.get(v.getId()));
            }
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
        System.out.println("Nb of vertices: "+ g.getNbVertices());
        return g;
    }
    
    public Vertex[] shortestPath(PlanningRequest pr) {
        Graph g = completeGraph(pr);
        System.out.println("Nb of vertices shortest path: "+ g.getNbVertices());
        TSP1 tsp = new TSP1();
        tsp.searchSolution(5000, g, g.getVertexById(pr.getDepot().getAddress().getId()));
        return tsp.getSolution();
    }
    
    public void tsp() {
        
    }
    
    public static void main (String[] args) {
        XmlReader reader = new XmlReader();
        reader.readMap("/users/zakaria/Downloads/fichiersXML2020/mediumMap.xml");
        GraphProcessor gp =  new GraphProcessor(reader.getMap());
        PlanningRequest pr = reader.readRequest("/users/zakaria/Downloads/fichiersXML2020/requestsMedium5.xml");
        Vertex[] sol = gp.shortestPath(pr);
        System.out.println(sol);
        for (int i=0; i<sol.length; i++){
            if (sol[i]!=null)
                System.out.println("Vertex " + i + ":" + sol[i].getId());
        }
        /*Graph g = new Graph();
        for(int i = 1; i<= 3000; i++){
            g.addVertex(new Vertex(new Long(i)));
        }
        System.out.println(g.getVertexMap());

        g.addEdge(1L,2L,7);
        g.addEdge(1L,3L,9);
        g.addEdge(1L,6L,14);
        g.addEdge(2L,3L,10);
        g.addEdge(3L,6L,2);
        g.addEdge(3L,4L,11);
        g.addEdge(2L,4L,15);
        g.addEdge(6L,5L,9);
        g.addEdge(4L,5L,6);

        for(int i = 7; i<=3000; i++){
            for(int j = 0; j<=Math.random()*6+1; j++){
                g.addEdge(new Long(i),(long)(Math.random()*3000+1),(float)(Math.random()*200+5));
            }
        }
       
        gp.graph = g; 
        List<Integer> goal = new ArrayList<>();
        goal.add(2);
        goal.add(3);
        goal.add(4);
        goal.add(5);
        goal.add(6);
        gp.shortestPath(pr);*/
    }
}
