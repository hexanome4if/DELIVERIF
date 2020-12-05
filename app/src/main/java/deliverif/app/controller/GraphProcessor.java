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

    private Graph graph;
    private Map map;
    private HashMap<String, VertexPath> fullPath;

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
    }

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

        while (!gris.isEmpty() && !noir.containsAll(goals)) {  //continue s'il reste des noeuds gris ou il reste des noeuds non-déterminé dans la liste goal
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
        tsp.searchSolution(5000, g, g.getVertexById(pr.getDepot().getAddress().getId()), ordre);
        return tsp;
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
        Tour tour = new Tour(pr);
        fullPath.clear();
        TSP1 tsp = hamiltonianCircuit(pr);
        Vertex[] sol = tsp.getSolution();
        double velocity = 15 * 1000 / 60;
        Calendar cal = Calendar.getInstance();
        cal.setTime(pr.getDepot().getDepartureTime());
        if (sol == null || sol[0] == null) {
            return null;
        }
        for (int i = 0; i < sol.length; i++) {
            System.out.println("Vertex " + i + ":" + sol[i].getId());
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
            double commute = path.getLength() / velocity;
            if (pickups.containsKey(next.getId())) {
                commute += pickups.get(next.getId());
            } else if (deliveries.containsKey(next.getId())) {
                commute += deliveries.get(next.getId());
            }
            cal.add(Calendar.MINUTE, (int) commute);
            path.setArrivalTime(cal.getTime());
            //System.out.println("Path" + i + ":" + path + "\n");
            tour.addPath(path);
        }
        // Adding path back to warehouse
        Intersection last = map.getIntersectionParId(sol[sol.length - 1].getId());
        Intersection warehouse = pr.getDepot().getAddress();
        System.out.println("Going from: " + last + "to: " + warehouse.getId());
        Path back = shortestPathBetweenTwoIntersections(last, warehouse);
        back.setDepatureTime(cal.getTime());
        double commute = back.getLength() / velocity;
        cal.add(Calendar.MINUTE, (int) commute);
        back.setArrivalTime(cal.getTime());
        tour.addPath(back);
        System.out.println("-----------End of Tour---------");
        return tour;
    }

    public Tour addRequestToTour(Tour tour, Request rqst) {
        PlanningRequest pr = tour.getPr();
        pr.getRequests().add(rqst);
        ArrayList<Path> paths = tour.getPaths();
        paths.remove(paths.size() - 1);
        Graph g = new Graph();
        Vertex lastPoint = graph.getVertexById(paths.get(paths.size() - 1).getArrival().getId());
        Vertex pickup = graph.getVertexById(rqst.getPickupAddress().getId());
        Vertex delivery = graph.getVertexById(rqst.getDeliveryAddress().getId());
        Vertex warehouse = graph.getVertexById(paths.get(0).getDeparture().getId());
        List<Vertex> goalPickup = new ArrayList<Vertex>();
        goalPickup.add(pickup);
        List<Vertex> goalDelivery = new ArrayList<Vertex>();
        goalDelivery.add(delivery);
        List<Vertex> goalWarehouse = new ArrayList<Vertex>();
        goalWarehouse.add(warehouse);
        dijkstra(g, lastPoint, goalPickup);
        dijkstra(g, pickup, goalDelivery);
        dijkstra(g, delivery, goalWarehouse);
        Path beforePickup = paths.get(paths.size() - 1);
        Path lastToPick = fullPath.get(lastPoint.getId() + "-" + pickup.getId()).convertToPath(map);
        Path pickToDeli = fullPath.get(pickup.getId() + "-" + delivery.getId()).convertToPath(map);
        Path deliToWrhs = fullPath.get(delivery.getId() + "-" + warehouse.getId()).convertToPath(map);

        Calendar cal = Calendar.getInstance();
        cal.setTime(beforePickup.getArrivalTime());

        lastToPick.setDepatureTime(cal.getTime());
        double velocity = 15 * 1000 / 60;
        double commute = lastToPick.getLength() / velocity;
        cal.add(Calendar.MINUTE, (int) commute);
        lastToPick.setArrivalTime(cal.getTime());
        cal.add(Calendar.MINUTE, rqst.getPickupDuration());
        lastToPick.setRequest(rqst);

        pickToDeli.setDepatureTime(cal.getTime());
        commute = pickToDeli.getLength() / velocity;
        cal.add(Calendar.MINUTE, (int) commute);
        pickToDeli.setArrivalTime(cal.getTime());
        cal.add(Calendar.MINUTE, rqst.getDeliveryDuration());
        lastToPick.setRequest(rqst);

        deliToWrhs.setDepatureTime(cal.getTime());
        commute = deliToWrhs.getLength() / velocity;
        cal.add(Calendar.MINUTE, (int) commute);
        deliToWrhs.setArrivalTime(cal.getTime());
        cal.add(Calendar.MINUTE, rqst.getDeliveryDuration());

        paths.add(lastToPick);
        paths.add(pickToDeli);
        paths.add(deliToWrhs);

        tour.setPaths(paths);
        tour.setPr(pr);
        return tour;
    }

    public Tour removeRequestFromTour(Tour t, Request r) {
        t.getPr().removeRequest(r);
        Intersection pickup = r.getPickupAddress();
        Intersection delivery = r.getDeliveryAddress();
        System.out.println("Pickup " + pickup);
        System.out.println("Delivery " + delivery);
        double velocity = 15 * 1000 / 60;
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
                //t.removePath(p);
                pickupIndex = i;
                i--;
            }
            if (p.getArrival().getId().equals(pickup.getId())) {
                beforePickup = p;
                //t.removePath(p);
                i--;
            }
            if (p.getDeparture().getId().equals(delivery.getId())) {
                afterDelivery = p;
                //t.removePath(p);
                deliveryIndex = i;
                i--;
            }
            if (p.getArrival().getId().equals(delivery.getId())) {
                beforeDelivery = p;
                //t.removePath(p);
                i--;
            }
            i++;
        }

        if (beforePickup == null || afterPickup == null || beforeDelivery == null || afterDelivery == null) {
            return t;
        }
        t.removePath(afterPickup);
        t.removePath(beforePickup);
        t.removePath(beforeDelivery);
        t.removePath(afterDelivery);
        if (afterPickup == beforeDelivery) {
            Path path = shortestPathBetweenTwoIntersections(beforePickup.getDeparture(), afterDelivery.getArrival());
            path.setDepatureTime(beforePickup.getDepatureTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(beforePickup.getDepatureTime());
            double commute = path.getLength() / velocity;
            cal.add(Calendar.MINUTE, (int) commute);
            path.setArrivalTime(cal.getTime());
            t.getPaths().add(pickupIndex, path);
        } else {
            Path pickupPath = shortestPathBetweenTwoIntersections(beforePickup.getDeparture(), afterPickup.getArrival());
            pickupPath.setDepatureTime(beforePickup.getDepatureTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(beforePickup.getDepatureTime());
            double commute = pickupPath.getLength() / velocity;
            cal.add(Calendar.MINUTE, (int) commute);
            pickupPath.setArrivalTime(cal.getTime());
            t.getPaths().add(pickupIndex, pickupPath);

            Path deliveryPath = shortestPathBetweenTwoIntersections(beforeDelivery.getDeparture(), afterDelivery.getArrival());
            deliveryPath.setDepatureTime(beforeDelivery.getDepatureTime());
            cal.setTime(beforeDelivery.getDepatureTime());
            commute = deliveryPath.getLength() / velocity;
            cal.add(Calendar.MINUTE, (int) commute);
            deliveryPath.setArrivalTime(cal.getTime());
            t.getPaths().add(deliveryIndex, deliveryPath);
        }

        return t;
    }

    public static void main(String[] args) {
        XmlReader reader = new XmlReader();
        reader.readMap("src/main/resources/deliverif/app/fichiersXML2020/largeMap.xml");
        GraphProcessor gp = new GraphProcessor(reader.getMap());
        PlanningRequest pr = reader.readRequest("src/main/resources/deliverif/app/fichiersXML2020/requestsLarge9.xml");
        Tour tour = gp.optimalTour(pr);
        //System.out.println("Tour before deletion: " + tour);
        System.out.println("Duration before deletion: " + tour.getTotalDuration());
        System.out.println("Distance before deletion: " + tour.getTotalDistance());
        System.out.println("Nb paths: " + tour.getPaths().size());
        //gp.removeRequestFromTour(tour, pr.getRequests().get(1));

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
    }
}
