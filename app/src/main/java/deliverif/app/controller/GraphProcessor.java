/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import deliverif.app.controller.tsp.GeneticAlgorithm.SalesmanGenome;
import deliverif.app.controller.tsp.GeneticAlgorithm.TravellingSalesman;
import deliverif.app.controller.tsp.GeneticAlgorithm.TravellingSalesman.SelectionType;
import deliverif.app.controller.tsp.BranchAndBound.TSP1;
import deliverif.app.controller.tsp.TourGenerator;
import deliverif.app.model.graph.Edge;
import deliverif.app.model.graph.Graph;
import deliverif.app.model.graph.Tour;
import deliverif.app.model.graph.Vertex;
import deliverif.app.model.graph.VertexPath;
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

    private final Graph graph;
    private final Map map;
    protected HashMap<String, VertexPath> fullPath;
    public List<Vertex> currentVertex;
    private TSP1 currentTsp;
    private PlanningRequest pr;

    /**
     * Constructor of GraphProcessor
     *
     * @param m the main map
     */
    public GraphProcessor(Map m) {
        graph = new Graph();
        map = m;
        for (Intersection i : m.getIntersections().values()) {
            graph.addVertex(new Vertex(i.getId()));
        }
        for (Segment s : m.getSegments()) {
            graph.addEdge(s.getOrigin().getId(), s.getDestination().getId(), s.getLength());
        }
        fullPath = new HashMap<>();
        currentVertex = new ArrayList<>();
    }

    /**
     * Calculate the shortest paths from {@code source} to every vertices 
     * in {@code goals} using Dijkstra's algorithm.
     * 
     * @param completeGraph the graph that stores the results
     * @param source the vertex of departure, in type of {@code Vertex}
     * @param goals a list of {@code Vertex} containing all targets
     * @see Graph
     * @see Vertex
     */
    public void dijkstra(Graph completeGraph, Vertex source, List<Vertex> goals) {
        // Strutures
        HashMap<Long, Float> dis = new HashMap<>();  //distance : <idNoeud, distance>
        HashMap<Long, Long> precedents = new HashMap<>();
        List<Vertex> gris = new ArrayList<>();          //liste des noeuds grisés (mais cet algo est sobre;)
        List<Vertex> noir = new ArrayList<>();         //liste des noeuds noircis

        // Initialisation
        source = graph.getVertexMap().get(source.getId());
        for (Edge edg : source.getAdj()) {
            dis.put(edg.dest.getId(), edg.cost);
            precedents.put(edg.dest.getId(), source.getId());
            gris.add(edg.dest);
        }
        System.out.println("Before dijkstra loop");
        noir.add(source);
        List<Vertex> found = new ArrayList<>();
        while (!gris.isEmpty() && found.size() < goals.size()) {  //continue s'il reste des noeuds gris ou il reste des noeuds non-déterminé dans la liste goal
            float dis_MIN = Float.POSITIVE_INFINITY;
            Vertex vertex = null;
            // Parcours de liste gris pour trouver le noeud dont la valeur "distance" est minimal
            for (Vertex v : gris) {
                if (dis.get(v.getId()) < dis_MIN) {
                    dis_MIN = dis.get(v.getId());
                    vertex = v;
                }
            }
            vertex = graph.getVertexMap().get(vertex.getId());

            for (Edge edg : vertex.getAdj()) {
                if (!noir.contains(edg.dest)) {
                    if (dis.containsKey(edg.dest.getId())) {
                        if (edg.cost + dis.get(vertex.getId()) < dis.get(edg.dest.getId())) { //MAJ si ce chemin est plus court
                            dis.replace(edg.dest.getId(), edg.cost + dis.get(vertex.getId()));
                            precedents.replace(edg.dest.getId(), vertex.getId());
                        }
                    } else {
                        dis.put(edg.dest.getId(), edg.cost + dis.get(vertex.getId())); //ajouter la valeur "distance" d'un noeud blanc et le griser
                        precedents.put(edg.dest.getId(), vertex.getId());
                        gris.add(edg.dest);
                    }
                }
            }

            gris.remove(vertex); //changement d'état gris->noir pour le noeud choisi
            noir.add(vertex);
            if (goals.indexOf(vertex) != -1) {
                found.add(vertex);
            }
        }
        System.out.println("After dijkstra loop");
        for (Vertex v : goals) {
            if (!Objects.equals(v.getId(), source.getId())) {
                completeGraph.addEdgeOneSide(source.getId(), v.getId(), dis.get(v.getId()));
                VertexPath path = new VertexPath();
                path.addVertex(v);
                Vertex prec = v;
                do {
                    Long precId = precedents.get(prec.getId());
                    prec = graph.getVertexById(precId);
                    if (prec != null) {
                        path.addVertex(prec);
                    }
                } while (prec != null && !Objects.equals(prec.getId(), source.getId()));
                fullPath.put(source.getId() + "-" + v.getId(), path);
            }
        }
    }

    /**
     * Create a complete graph with a planning request
     * <p> The returning graph is complete and includes every 
     * {@code Intersection} mentioned in the given 
     * {@code PlanningRequest}, in the form of {@code Vertex}.
     * @param pr the planning request
     * @return the object {@code Graph} after calculating with Dijkstra's Algo
     * @see PlanningRequest
     * @see Graph
     */
    public Graph completeGraph(PlanningRequest pr) {
        Graph g = new Graph();
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(new Vertex(pr.getDepot().getAddress().getId()));
        for (Request r : pr.getRequests()) {
            vertices.add(new Vertex(r.getPickupAddress().getId()));
            vertices.add(new Vertex(r.getDeliveryAddress().getId()));
        }
        for (Vertex v : vertices) {
            g.addVertex(v);
        }
        System.out.println("Complete graph");
        for (Vertex v : vertices) {
            dijkstra(g, v, vertices);
        }
        System.out.println("Ok");
        return g;
    }

    /**
     * Create a template to resolve the Travelling Salesman Problem
     * @param pr the planning request
     * @return the template {@code TSP1}
     * @see TSP1
     */
    public TSP1 hamiltonianCircuit(PlanningRequest pr) {

        TSP1 tsp = new TSP1();

        return tsp;
    }

    /**
     * Create a template of TSP which uses genetic algorithms
     * @param pr the planning request
     * @return the list of result
     * @see TravellingSalesman
     * @see SalesmanGenome
     */
    public Vertex[] hamiltonianCircuit2(PlanningRequest pr) {
        Graph g = completeGraph(pr);
        List<Long> ordre = new ArrayList<>();
        for (Request r : pr.getRequests()) {
            ordre.add(r.getPickupAddress().getId());
            ordre.add(r.getDeliveryAddress().getId());
        }
        TravellingSalesman tsp = new TravellingSalesman(SelectionType.ROULETTE, g, g.getVertexById(pr.getDepot().getAddress().getId()), ordre, 8000);
        SalesmanGenome genome = tsp.optimise();
        List<Vertex> listResult = genome.getGenome();
        Vertex[] result = new Vertex[listResult.size()];
        int i = 0;
        for (Vertex v : listResult) {
            result[i++] = v;
        }
        return result;
    }

    /**
     * Get the shortest path between two intersections
     * <p> Get the path registered in the HashMap {@code fullPath},
     * it's different from {@code getNewPath}.
     * @param v1 the starting intersection
     * @param v2 the ending intersection
     * @return the object {@code Path} which is the shortest and links these
     * two intersection
     */
    public Path shortestPathBetweenTwoIntersections(Intersection v1, Intersection v2) {
        Vertex source = graph.getVertexById(v1.getId());
        Vertex destination = graph.getVertexById(v2.getId());
        System.out.println("source:" + source.getId());
        System.out.println("dest:" + destination.getId());
        VertexPath vertexPath = fullPath.get(source.getId().toString() + "-" + destination.getId().toString());
        
        if(vertexPath == null){
            System.out.println("VP null");
        }
        
        System.out.println("Made it past vertextPath");
        Path path = vertexPath.convertToPath(map);
        System.out.println("Conversion réussie");
        path.setDeparture(v1);
        path.setArrival(v2);
        return path;
    }

    /**
     * Generate the optimal tour
     * @param pr the planning request
     * @return the tour generator
     * @see TourGenerator
     */
    public TourGenerator optimalTour(PlanningRequest pr) {
        System.out.println("Init");
        currentVertex.clear();
        this.pr = pr;
        fullPath.clear();
        currentTsp = hamiltonianCircuit(pr);
        TourGenerator tourGenerator = new TourGenerator(currentTsp, pr, this, map);
        System.out.println("Init good");
        return tourGenerator;
    }

    /**
     * Start the TSP algorithm
     * @see TSP1
     */
    public void startAlgo() {
        System.out.println("Start");
        Graph g = completeGraph(pr);
        List<Long> ordre = new ArrayList<>();
        for (Request r : pr.getRequests()) {
            ordre.add(r.getPickupAddress().getId());
            ordre.add(r.getDeliveryAddress().getId());
        }
        System.out.println("Ok");
        currentTsp.searchSolution(75000, g, g.getVertexById(pr.getDepot().getAddress().getId()), ordre);
    }

    /**
     * Rearrange an existing request with a given order 
     * <p> The new order is a list of identity in type of {@code Long}, 
     * it should be starting and ending with the ID of the intersection
     * of {@code Depot}, and its length has to be the same as the 
     * original length of {@code Tour.paths}.
     * @param tour the original tour
     * @param newOrder the order of intersections that will be applied
     * @return the object {@code Tour} after changing, {@code null} if the
     * rules above are not respected.
     * @see Tour
     */
    public Tour changeOrder(Tour tour, List<Long> newOrder) {
        //newOrder === depot -> nodes -> depot
        if (newOrder.size() != tour.getOrder().size()) {
            System.out.println("not the same length!!!!!!");
        }
        newOrder.add(newOrder.get(0));
        Tour newTour = new Tour(tour);
        ArrayList<Path> newPaths = new ArrayList<>();
        for (int i = 0; i < newOrder.size() - 1; i++) {
            newPaths.add(getNewPath(newOrder.get(i), newOrder.get(i+1)));
        }
        newTour.setPaths(newPaths);
        newTour.update();// synchroniser les horaires
        
        return newTour;
    }

    /**
     * Get the shortest path between two intersections
     * <p> A sub-method of {@code changeOrder}, it's different from 
     * {@code shortestPathBetweenTwoIntersections} because it uses the
     * method {@code dijkstra}.
     * @param idStart the identity of the starting intersection
     * @param idStop the identity of the ending intersection
     * @return the object {@code Path} which is the shortest and links these
     * two intersection
     */
    public Path getNewPath(Long idStart, Long idStop) {
        Graph g = new Graph();
        Vertex start = graph.getVertexById(idStart);
        Vertex stop = graph.getVertexById(idStop);
        List<Vertex> goal = new ArrayList<>();
        goal.add(stop);
        dijkstra(g, start, goal);
        return fullPath.get(idStart + "-" + idStop).convertToPath(map);
    }

    /**
     * Add a new request to the circuit after the delivery begins
     * <p> The added one will be placed at the last of the tour 
     * in order to keep the agreed arrangements.
     * @param tour the original tour
     * @param rqst the request that will be added
     * @return the object {@code Tour} after inserting
     * @see Tour
     */
    public Tour addRequestToTour(Tour tour, Request rqst) {
        tour.getPr().addRequest(rqst);
        tour.removePath(tour.getPaths().get(tour.getPaths().size() - 1));
        Vertex lastPoint = graph.getVertexById(tour.getPaths().get(tour.getPaths().size() - 1).getArrival().getId());
        Vertex pickup = graph.getVertexById(rqst.getPickupAddress().getId());
        Vertex delivery = graph.getVertexById(rqst.getDeliveryAddress().getId());
        Vertex warehouse = graph.getVertexById(tour.getPaths().get(0).getDeparture().getId());

        List<Vertex> goals = new ArrayList<>();
        goals.add(pickup);
        goals.add(delivery);
        for (Vertex v : currentVertex) {
            dijkstra(new Graph(), v, goals);
        }
        currentVertex.add(pickup);
        currentVertex.add(delivery);

        dijkstra(new Graph(), pickup, currentVertex);
        dijkstra(new Graph(), delivery, currentVertex);

        Path beforePickup = tour.getPaths().get(tour.getPaths().size() - 1);
        Path lastToPick = fullPath.get(lastPoint.getId() + "-" + pickup.getId()).convertToPath(map);
        lastToPick.setDeparture(map.getIntersectionParId(lastPoint.getId()));
        lastToPick.setArrival(map.getIntersectionParId(pickup.getId()));
        Path pickToDeli = fullPath.get(pickup.getId() + "-" + delivery.getId()).convertToPath(map);
        pickToDeli.setDeparture(map.getIntersectionParId(pickup.getId()));
        pickToDeli.setArrival(map.getIntersectionParId(delivery.getId()));
        Path deliToWrhs = fullPath.get(delivery.getId() + "-" + warehouse.getId()).convertToPath(map);
        deliToWrhs.setDeparture(map.getIntersectionParId(delivery.getId()));
        deliToWrhs.setArrival(map.getIntersectionParId(warehouse.getId()));

        Calendar cal = Calendar.getInstance();
        cal.setTime(beforePickup.getArrivalTime());
        int lastTimeToAdd = 0;
        for (Request r : tour.getPr().getRequests()) {
            if (r.getDeliveryAddress().getId().equals(beforePickup.getArrival().getId())) {
                lastTimeToAdd = r.getDeliveryDuration();
                break;
            }
            if (r.getPickupAddress().getId().equals(beforePickup.getArrival().getId())) {
                lastTimeToAdd = r.getPickupDuration();
                break;
            }
        }
        cal.add(Calendar.SECOND, lastTimeToAdd);
        lastToPick.setDepatureTime(cal.getTime());
        double velocity = 15 * 1000 / 3600;

        double commute = lastToPick.getLength() / velocity;
        cal.add(Calendar.SECOND, (int) commute);
        lastToPick.setArrivalTime(cal.getTime());
        cal.add(Calendar.SECOND, rqst.getPickupDuration());
        lastToPick.setRequest(rqst);

        pickToDeli.setDepatureTime(cal.getTime());
        commute = pickToDeli.getLength() / velocity;
        cal.add(Calendar.SECOND, (int) commute);
        pickToDeli.setArrivalTime(cal.getTime());
        cal.add(Calendar.SECOND, rqst.getDeliveryDuration());
        lastToPick.setRequest(rqst);

        deliToWrhs.setDepatureTime(cal.getTime());
        commute = deliToWrhs.getLength() / velocity;
        cal.add(Calendar.SECOND, (int) commute);
        deliToWrhs.setArrivalTime(cal.getTime());
        cal.add(Calendar.SECOND, rqst.getDeliveryDuration());

        tour.addPath(lastToPick);
        tour.addPath(pickToDeli);
        tour.addPath(deliToWrhs);
        return tour;
    }

    /**
     * Remove an existing request from the circuit
     * @param t the original tour
     * @param r the request that will be cancelled
     * @return the object {@code Tour} after cancelling
     * @see Tour
     */
    public Tour removeRequestFromTour(Tour t, Request r) {
        t.getPr().removeRequest(r);
        Intersection pickup = r.getPickupAddress();
        Intersection delivery = r.getDeliveryAddress();
        double velocity = 15 * 1000 / 3600;
        Path beforePickup = null;
        Path afterPickup = null;
        Path beforeDelivery = null;
        Path afterDelivery = null;
        int i = 0;
        int deliveryIndex = 0;
        int pickupIndex = 0;
        for (Path p : t.getPaths()) {
            if (p.getDeparture().getId().equals(pickup.getId())) {
                afterPickup = p;
                pickupIndex = i;
            }
            if (p.getArrival().getId().equals(pickup.getId())) {
                beforePickup = p;
            }
            if (p.getDeparture().getId().equals(delivery.getId())) {
                afterDelivery = p;
                deliveryIndex = i;
            }
            if (p.getArrival().getId().equals(delivery.getId())) {
                beforeDelivery = p;
            }
            i++;
        }

        if (beforePickup == null || afterPickup == null || beforeDelivery == null || afterDelivery == null) {
            System.out.println("Problem");
            System.out.println(beforePickup);
            System.out.println(afterPickup);
            System.out.println(beforeDelivery);
            System.out.println(afterDelivery);
            return t;
        }
        if (afterPickup == beforeDelivery) {
            Path path = shortestPathBetweenTwoIntersections(beforePickup.getDeparture(), afterDelivery.getArrival());
            path.setDepatureTime(beforePickup.getDepatureTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(beforePickup.getDepatureTime());
            double commute = path.getLength() / velocity;
            cal.add(Calendar.SECOND, (int) commute);
            path.setArrivalTime(cal.getTime());
            t.getPaths().add(pickupIndex, path);
        } else {
            Path pickupPath = shortestPathBetweenTwoIntersections(beforePickup.getDeparture(), afterPickup.getArrival());
            pickupPath.setDepatureTime(beforePickup.getDepatureTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(beforePickup.getDepatureTime());
            double commute = pickupPath.getLength() / velocity;
            cal.add(Calendar.SECOND, (int) commute);
            pickupPath.setArrivalTime(cal.getTime());
            t.getPaths().add(pickupIndex, pickupPath);

            Path deliveryPath = shortestPathBetweenTwoIntersections(beforeDelivery.getDeparture(), afterDelivery.getArrival());
            deliveryPath.setDepatureTime(beforeDelivery.getDepatureTime());
            cal.setTime(beforeDelivery.getDepatureTime());
            commute = deliveryPath.getLength() / velocity;
            cal.add(Calendar.SECOND, (int) commute);
            deliveryPath.setArrivalTime(cal.getTime());
            t.getPaths().add(deliveryIndex, deliveryPath);
        }
        t.removePath(afterPickup);
        t.removePath(beforePickup);
        t.removePath(beforeDelivery);
        t.removePath(afterDelivery);

        return t;
    }

    /**
     * Get the current map
     * @return current map
     */
    public Map getMap() {
        return map;
    }
}
