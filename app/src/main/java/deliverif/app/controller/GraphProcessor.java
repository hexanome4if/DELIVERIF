/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import deliverif.app.controller.tsp.GeneticAlgorithm.SalesmanGenome;
import deliverif.app.controller.tsp.GeneticAlgorithm.TravellingSalesman;
import deliverif.app.controller.tsp.GeneticAlgorithm.TravellingSalesman.SelectionType;
import deliverif.app.controller.tsp.TSP1;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author zakaria
 */
public class GraphProcessor {

    private Graph graph;
    private Map map;
    private HashMap<String, VertexPath> fullPath;
    private List<Vertex> currentVertex;

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
     *
     * @param completeGraph
     * @param source
     * @param goals
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

        for (Vertex v : vertices) {
            dijkstra(g, v, vertices);
        }
        return g;
    }

    public TSP1 hamiltonianCircuit(PlanningRequest pr) {
        Graph g = completeGraph(pr);
        TSP1 tsp = new TSP1();

        List<Long> ordre = new ArrayList<>();
        for (Request r : pr.getRequests()) {
            ordre.add(r.getPickupAddress().getId());
            ordre.add(r.getDeliveryAddress().getId());
        }
        tsp.searchSolution(75000, g, g.getVertexById(pr.getDepot().getAddress().getId()), ordre);
        return tsp;
    }

    public Vertex[] hamiltonianCircuit2(PlanningRequest pr) {
        Graph g = completeGraph(pr);
        List<Long> ordre = new ArrayList<>();
        for (Request r : pr.getRequests()) {
            ordre.add(r.getPickupAddress().getId());
            ordre.add(r.getDeliveryAddress().getId());
        }
        TravellingSalesman tsp = new TravellingSalesman(SelectionType.ROULETTE, g, g.getVertexById(pr.getDepot().getAddress().getId()), ordre, 4000);
        SalesmanGenome genome = tsp.optimise();
        List<Vertex> listResult = genome.getGenome();
        Vertex[] result = new Vertex[listResult.size()];
        int i = 0;
        for (Vertex v : listResult) {
            result[i++] = v;
        }
        return result;
    }

    public Path shortestPathBetweenTwoIntersections(Intersection v1, Intersection v2) {
        Vertex source = graph.getVertexById(v1.getId());
        Vertex destination = graph.getVertexById(v2.getId());

        VertexPath vertexPath = fullPath.get(source.getId().toString() + "-" + destination.getId().toString());
        Path path = vertexPath.convertToPath(map);
        path.setDeparture(v1);
        path.setArrival(v2);
        return path;
    }

    public Tour optimalTour(PlanningRequest pr) {
        currentVertex.clear();
        Tour tour = new Tour(pr);
        fullPath.clear();
        TSP1 tsp = hamiltonianCircuit(pr);
        Vertex[] sol = tsp.getSolution();
        //Vertex[] sol = hamiltonianCircuit2(pr);
        double velocity = 15 * 1000 / 3600;
        Calendar cal = Calendar.getInstance();
        cal.setTime(pr.getDepot().getDepartureTime());
        if (sol == null || sol[0] == null) {
            return null;
        }
        for (int i = 0; i < sol.length; i++) {
            System.out.println("Vertex " + i + ":" + sol[i].getId());
            currentVertex.add(sol[i]);
        }
        HashMap<Long, Integer> pickups = new HashMap<>();
        HashMap<Long, Integer> deliveries = new HashMap<>();
        for (Request r : pr.getRequests()) {
            pickups.put(r.getPickupAddress().getId(), r.getPickupDuration());
            deliveries.put(r.getDeliveryAddress().getId(), r.getDeliveryDuration());
        }
        System.out.println("---------Tour Deets---------");
        // Adding paths excluding warehouse
        for (int i = 0; i < sol.length - 1; i++) {
            Intersection curr = map.getIntersectionParId(sol[i].getId());
            Intersection next = map.getIntersectionParId(sol[i + 1].getId());
            System.out.println("Going from: " + curr.getId() + " to: " + next.getId());
            Path path = shortestPathBetweenTwoIntersections(curr, next);
            path.setDepatureTime(cal.getTime());
            int commute = (int) (path.getLength() / velocity);

            cal.add(Calendar.SECOND, commute);
            path.setArrivalTime(cal.getTime());
            if (pickups.containsKey(next.getId())) {
                commute = pickups.get(next.getId());
            } else if (deliveries.containsKey(next.getId())) {
                commute = deliveries.get(next.getId());
            }
            cal.add(Calendar.SECOND, commute);
            //System.out.println("Path" + i + ":" + path + "\n");
            tour.addPath(path);
        }
        // Adding path back to warehouse
        Intersection last = map.getIntersectionParId(sol[sol.length - 1].getId());
        Intersection warehouse = pr.getDepot().getAddress();
        System.out.println("Going from: " + last + "to: " + warehouse.getId());
        Path back = shortestPathBetweenTwoIntersections(last, warehouse);
        back.setDepatureTime(cal.getTime());
        int commute = (int) (back.getLength() / velocity);
        cal.add(Calendar.SECOND, commute);
        back.setArrivalTime(cal.getTime());
        tour.addPath(back);
        System.out.println("-----------End of Tour---------");
        return tour;
    }

    public Tour changeOrder(Tour tour, List<Long> newOrder) {
        //newOrder === depot -> nodes -> depot
        if (newOrder.size() != tour.getOrder().size()) {
            System.out.println("not the same length!!!!!!");
        }
        Tour newTour = new Tour(tour);
        ArrayList<Path> newPaths = new ArrayList<>();
        for (int i = 0; i < newOrder.size() - 1; i++) {
            newPaths.add(getNewPath(newOrder.get(i + 1), newOrder.get(i)));
        }
        newTour.setPaths(newPaths);
        newTour.update();// synchroniser les horaires

        return newTour;
    }

    public Path getNewPath(Long idStart, Long idStop) {
        Graph g = new Graph();
        Vertex start = graph.getVertexById(idStart);
        Vertex stop = graph.getVertexById(idStop);
        List<Vertex> goal = new ArrayList<>();
        goal.add(stop);
        dijkstra(g, start, goal);
        return fullPath.get(idStart + "-" + idStop).convertToPath(map);
    }

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

    public static void main(String[] args) {
        XmlReader reader = new XmlReader();
        reader.readMap("src/main/resources/deliverif/app/fichiersXML2020/smallMap.xml");
        GraphProcessor gp = new GraphProcessor(reader.getMap());
        PlanningRequest pr = reader.readRequest("src/main/resources/deliverif/app/fichiersXML2020/requestsSmall2.xml");
        Tour tour = gp.optimalTour(pr);
        //System.out.println("Tour before deletion: " + tour);
        System.out.println("Duration before deletion: " + tour.getTotalDuration());
        System.out.println("Distance before deletion: " + tour.getTotalDistance());
        System.out.println("Nb paths: " + tour.getPaths().size());
        //gp.removeRequestFromTour(tour, pr.getRequests().get(1));

        //RemoveRequestCommand rr = new RemoveRequestCommand(gp, tour, pr.getRequests().get(1));
        /*
        RemoveRequest rr = new RemoveRequest(gp, tour, pr.getRequests().get(1));
        rr.doCommand();
        System.out.println("Duration after deletion: " + tour.getTotalDuration());
        System.out.println("Distance after deletion: " + tour.getTotalDistance());
        System.out.println("Nb paths: " + tour.getPaths().size());
        rr.undoCommand();
        System.out.println("Duration after undo: " + tour.getTotalDuration());
        System.out.println("Distance after undo: " + tour.getTotalDistance());
        System.out.println("Nb paths: " + tour.getPaths().size());
        //System.out.println("Tour after deletion: " + tour);
         */
        List<Long> newOrder = new ArrayList<>();
        for (Path p : tour.getPaths()) {
            newOrder.add(p.getArrival().getId());
        }
        newOrder.remove(newOrder.size() - 1);
        Collections.shuffle(newOrder);
        newOrder.add(0, tour.getPr().getDepot().getAddress().getId());
        newOrder.add(tour.getPr().getDepot().getAddress().getId());
        for (int i = 1; i <= newOrder.size(); i++) {
            System.out.println("Node " + i + ": " + newOrder.get(i - 1).toString());
        }

        Tour newTour = gp.changeOrder(tour, newOrder);
        System.out.println("Duration after change: " + newTour.getTotalDuration());
        System.out.println("Distance after change: " + newTour.getTotalDistance());
        System.out.println("Nb paths: " + newTour.getPaths().size());

        for (Path p : newTour.getPaths()) {
            System.out.println(p);
        }
    }
}
