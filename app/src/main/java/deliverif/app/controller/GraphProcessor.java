/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import deliverif.app.controller.tsp.TSP1;
import deliverif.app.model.graph.Edge;
import deliverif.app.model.graph.Graph;
import deliverif.app.model.graph.Tour;
import deliverif.app.model.graph.Vertex;
import deliverif.app.model.map.Intersection;
import deliverif.app.model.map.Map;
import deliverif.app.model.map.Segment;
import deliverif.app.model.request.Path;
import deliverif.app.model.request.PlanningRequest;
import deliverif.app.model.request.Request;
import java.util.ArrayList;
import java.util.Calendar;
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
        
        // Initialisation
        source = graph.getVertexMap().get(source.getId());
        for(Edge edg : source.getAdj()){
            dis.put(edg.dest.getId(),edg.cost);
            gris.add(edg.dest);
        }
        noir.add(source);
        
        while(!gris.isEmpty() && !noir.containsAll(goals)){  //continue s'il reste des noeuds gris ou il reste des noeuds non-déterminé dans la liste goal
            float dis_MIN = Float.POSITIVE_INFINITY;
            Vertex vertex = null;
            for(Vertex v : gris){ //parcours de liste gris pour trouver le noeud dont la valeur "distance" est minimal
                if(dis.get(v.getId()) < dis_MIN){
                    dis_MIN = dis.get(v.getId());
                    vertex = v;
                }
            }
            vertex = graph.getVertexMap().get(vertex.getId());
            
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
        }
        for(Vertex v : goals){
            if(Objects.equals(v.getId(), source.getId())) {
            } else {
                g.addEdgeOneSide(source.getId(), v.getId(), dis.get(v.getId()));
            }
        }
    }
    
    public Graph completeGraph(PlanningRequest pr) {
        Graph g = new Graph();
        HashMap<Long, Vertex> vertexMap = graph.getVertexMap();
        List<Vertex> vertices = new ArrayList<>();
        vertices.add( new Vertex(pr.getDepot().getAddress().getId()) );
        for (Request r : pr.getRequests()){
            vertices.add( new Vertex(r.getPickupAddress().getId()));
            vertices.add( new Vertex(r.getDeliveryAddress().getId()));
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
    
    public TSP1 hamiltonianCircuit (PlanningRequest pr) {
        Graph g = completeGraph(pr);
        System.out.println("Nb of vertices shortest path: "+ g.getNbVertices());
        TSP1 tsp = new TSP1();
        System.out.println("start at: " + pr.getDepot().getAddress().getId());
        for(Edge e : g.getVertexById(pr.getDepot().getAddress().getId()).getAdj()){
            System.out.println("to " + e.dest.getId() + " cost: "+ e.cost);
        }
        List<Long> ordre = new ArrayList<>();
        for (Request r : pr.getRequests()){
            ordre.add( r.getPickupAddress().getId());
            ordre.add( r.getDeliveryAddress().getId());
        }
        tsp.searchSolution(5000, g, g.getVertexById(pr.getDepot().getAddress().getId()), ordre);
        return tsp;
    }
    
    public Path dijkstraPath(Graph g, Vertex source, Vertex goal){
        // Structures
        HashMap<Long, Float> dis = new HashMap<>();  //distance : <idNoeud, distance>
        HashMap<Long, List<Long>> parcours =  new HashMap<>();
        List<Vertex> gris = new ArrayList<>();          //liste des noeuds grisés (mais cet algo est sobre;)
        List<Vertex> noir = new ArrayList<>();         //liste des noeuds noircis
        HashMap<Long, Path> paths = new HashMap<>();
        HashMap<Long,Long> precedents = new HashMap<>();
        
        // Initialisation
        source = graph.getVertexMap().get(source.getId());
        
        //List<Long> initParcours = new ArrayList<>();
        //initParcours.add(source.getId()); //p
        
        
        Intersection sourceItsc = map.getIntersectionParId(source.getId());
        for(Edge edg : source.getAdj()){
            dis.put(edg.dest.getId(),edg.cost);
            //parcours.put(edg.dest.getId(),initParcours);//p
            precedents.put(edg.dest.getId(), sourceItsc.getId()); 
            gris.add(edg.dest);
        }
        noir.add(source);
        
        // Itérations
        while(!gris.isEmpty() && !noir.contains(goal)){  //continue s'il reste des noeuds gris ou il reste des noeuds non-déterminé dans la liste goal
            float dis_MIN = Float.POSITIVE_INFINITY;
            Vertex vertex = null;
            for(Vertex v : gris){ //parcours de liste gris pour trouver le noeud dont la valeur "distance" est minimal
                if(dis.get(v.getId()) < dis_MIN){
                    dis_MIN = dis.get(v.getId());
                    vertex = v;
                }
            }
            vertex = graph.getVertexMap().get(vertex.getId());
            
            for(Edge edg : vertex.getAdj()){
                if(dis.containsKey(edg.dest.getId())){
                    if(edg.cost + dis.get(vertex.getId()) < dis.get(edg.dest.getId())){ //MAJ si ce chemin est plus court
                        dis.put(edg.dest.getId(), edg.cost + dis.get(vertex.getId()));
                        
                        /*List<Long> intermediareParcours = parcours.get(vertex.getId());
                        intermediareParcours.add(vertex.getId());
                        parcours.put(edg.dest.getId(), intermediareParcours);*/
                        precedents.put(edg.dest.getId(), vertex.getId());                     
                    } 
                } else {
                    dis.put(edg.dest.getId(), edg.cost + dis.get(vertex.getId())); //ajouter la valeur "distance" d'un noeud blanc et le griser
                    precedents.put(edg.dest.getId(), vertex.getId());
                    /*List<Long> intermediareParcours = parcours.get(vertex.getId());//p
                    intermediareParcours.add(vertex.getId());
                    parcours.put(edg.dest.getId(), intermediareParcours);*/
                    gris.add(edg.dest);
                    
                }
            }
            
            gris.remove(vertex); //changement d'état gris->noir pour le noeud choisi
            noir.add(vertex);
        }
        System.out.println("Source: " + source.getId());
        System.out.println("Goal: " + goal.getId());
        System.out.println(precedents);
        Path path = new Path();
        Long next = goal.getId();
        do {
            path.addSegment(map.getSegmentParExtremites(map.getIntersectionParId(next), 
                    map.getIntersectionParId(precedents.get(next))));
            next = precedents.get(next);
        } while(next != source.getId());
        return path;
        
    }
    
    public Path shortestPathBetweenTwoIntersections (Intersection v1, Intersection v2) {
        return dijkstraPath(graph, graph.getVertexById(v1.getId()), graph.getVertexById(v2.getId()));
    }
    
    public Tour optimalTour (PlanningRequest pr) {
        Tour tour = new Tour (pr);
        GraphProcessor gp =  new GraphProcessor(map);
        TSP1 tsp = gp.hamiltonianCircuit(pr);
        Vertex[] sol = tsp.getSolution();
        double velocity = 15 * 1000 / 60;
        Calendar cal =  Calendar.getInstance();
        cal.setTime(pr.getDepot().getDepartureTime());
        if (sol == null || sol[0] == null) return null;
        Long arrivalId = null;
        for (int i=0; i<sol.length; i++){
            System.out.println("Vertex " + i + ":" + sol[i].getId());
        }
        // Adding paths excluding warehouse
        for (int i=0; i<sol.length-1; i++){
            Intersection curr = map.getIntersectionParId(sol[i].getId());
            Intersection next = map.getIntersectionParId(sol[i+1].getId());
            Path path = shortestPathBetweenTwoIntersections(curr,next);
            path.setDepatureTime(cal.getTime());
            double cycling = path.getLength()/velocity;
            cal.add(Calendar.MINUTE, (int) cycling);
            path.setArrivalTime(cal.getTime());
            //System.out.println("Path" + i + ":" + path + "\n");
            tour.addPath(path);
            // Need to take into account path type to add pickup/delivery time
        }
        Intersection secondlast =  map.getIntersectionParId(sol[sol.length-2].getId());
        Intersection last = map.getIntersectionParId(sol[sol.length-1].getId());
        Path path = shortestPathBetweenTwoIntersections(secondlast, last);
        path.setDepatureTime(cal.getTime());
        double cycling = path.getLength()/velocity;
        cal.add(Calendar.MINUTE, (int) cycling);
        path.setArrivalTime(cal.getTime());
        tour.addPath(path);
        // Adding path back to warehouse
        Intersection warehouse =  pr.getDepot().getAddress();
        Path back = shortestPathBetweenTwoIntersections(last, warehouse);
        back.setDepatureTime(cal.getTime());
        double cyclingback = back.getLength()/velocity;
        cal.add(Calendar.MINUTE, (int) cyclingback);
        path.setArrivalTime(cal.getTime());
        tour.addPath(back);
        
        return tour;
    }
    
    public static void main (String[] args) {
        XmlReader reader = new XmlReader();
        reader.readMap("src/main/resources/deliverif/app/fichiersXML2020/smallMap.xml");
        GraphProcessor gp =  new GraphProcessor(reader.getMap());
        PlanningRequest pr = reader.readRequest("src/main/resources/deliverif/app/fichiersXML2020/requestsSmall1.xml");
        
        Tour tour = gp.optimalTour(pr);
        System.out.println("Tour: "+tour);
        TSP1 tsp = gp.hamiltonianCircuit(pr);
        Vertex[] sol = tsp.getSolution();
        float cost = tsp.getSolutionCost();
        System.out.println("Total cost: " + cost);
        int i;
        for (i=0; i<sol.length; i++){
                System.out.println("Vertex " + i + ":" + sol[i].getId());
        }
        System.out.println("Vertex " + i + ":" + pr.getDepot().getAddress().getId());
        /*TSP1 tsp = gp.hamiltonianCircuit(pr);
        Vertex[] sol = tsp.getSolution();
        float cost = tsp.getSolutionCost();
        System.out.println("Total cost: " + cost);
        for (int i=0; i<sol.length; i++){
            if (sol[i]!=null)
                System.out.println("Vertex " + i + ":" + sol[i].getId());
        }*/
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
