/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import deliverif.app.controller.tsp.BranchAndBound.TSP1;
import deliverif.app.controller.tsp.TourGenerator;
import deliverif.app.model.graph.Graph;
import deliverif.app.model.graph.Tour;
import deliverif.app.model.graph.Vertex;
import deliverif.app.model.graph.VertexPath;
import deliverif.app.model.map.Intersection;
import deliverif.app.model.map.Map;
import deliverif.app.model.request.Path;
import deliverif.app.model.request.PlanningRequest;
import deliverif.app.model.request.Request;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author zakaria
 */
public class GraphProcessorTest {
    
    private HashMap<String, VertexPath> fullPath;
    
    public GraphProcessorTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
        if (fullPath != null)  fullPath.clear();
    }
    
    
    /**
     * Test of completeGraph method, of class GraphProcessor.
     */
    @Test
    public void testCompleteGraph() {
        System.out.println("completeGraph");
        XmlReader reader = new XmlReader();
        reader.readMap("src/main/resources/deliverif/app/fichiersXML2020/smallMap.xml");
        GraphProcessor instance = new GraphProcessor(reader.getMap());
        PlanningRequest pr = reader.readRequest("src/main/resources/deliverif/app/fichiersXML2020/requestsSmall1.xml");
        
        assertNotEquals(pr, null);
        assertNotEquals(reader.getMap(), null);
        assertNotEquals(instance, null);
        
        Graph result = instance.completeGraph(pr);
        Graph expResult = new Graph();
        expResult.addVertex(new Vertex(342873658L));
        expResult.addVertex(new Vertex(208769039L));
        expResult.addVertex(new Vertex(25173820L));
        expResult.addEdge(342873658L, 208769039L, (float) 231.60083);
        expResult.addEdge(342873658L, 25173820L, (float) 2330.1206);
        expResult.addEdge(25173820L, 208769039L, (float) 2202.1833);
        assertEquals(expResult.toString(), result.toString());
    }

    /**
     * Test of dijkstra method, of class GraphProcessor.
     */
    @Test
    public void testDijkstra() {
        System.out.println("dijkstra");
        // Load map and requests
        XmlReader reader = new XmlReader();
        reader.readMap("src/main/resources/deliverif/app/fichiersXML2020/smallMap.xml");
        GraphProcessor gp = new GraphProcessor(reader.getMap());
        Graph graphVide = new Graph();
        Graph graphExp = new Graph();
        PlanningRequest pr = reader.readRequest("src/main/resources/deliverif/app/fichiersXML2020/requestsSmall1.xml");
        Tour tour = new Tour(pr);
        
        assertNotEquals(pr, null);
        assertNotEquals(reader.getMap(), null);
        assertNotEquals(gp, null);
        
        // Initialize entry data and the expected object
        Vertex source = new Vertex(208769457L);
        Vertex dest1 = new Vertex(1679901320L);
        Vertex dest2 = new Vertex(208769120L);
        Vertex dest3 = new Vertex(25336179L);
        Vertex dest4 = new Vertex(208769039L);
        List<Vertex> goals = new ArrayList<>();
        goals.add(dest1);
        goals.add(dest2);
        goals.add(dest3);
        goals.add(dest4);
        
        graphVide.addVertex(source);
        graphExp.addVertex(source);
        for(Vertex v : goals){
            graphVide.addVertex(v);
            graphExp.addVertex(v);
        }
        
        graphExp.addEdgeOneSide(source.getId(), dest1.getId(), (float) 411.126);
        graphExp.addEdgeOneSide(source.getId(), dest2.getId(), (float) 691.148);
        graphExp.addEdgeOneSide(source.getId(), dest3.getId(), (float) 1539.744);
        graphExp.addEdgeOneSide(source.getId(), dest4.getId(), (float) 207.214);
        
        // Run dijkstra and test
        gp.dijkstra(graphVide, source, goals);
        assertEquals(graphVide.toString(),graphExp.toString());
        
        // Test fullPath feature
        Path p1 = new Path();
        p1.setDeparture(reader.getMap().getIntersectionParId(source.getId()));
        p1.setArrival(reader.getMap().getIntersectionParId(dest1.getId()));
        p1.setLength((float)411.12595);
        Path p2 = new Path(p1);
        p2.setArrival(reader.getMap().getIntersectionParId(dest2.getId()));
        p2.setLength((float)691.1477);
        Path p3 = new Path(p1);
        p3.setArrival(reader.getMap().getIntersectionParId(dest3.getId()));
        p3.setLength((float)1539.7443);
        Path p4 = new Path(p1);
        p4.setArrival(reader.getMap().getIntersectionParId(dest4.getId()));
        p4.setLength((float)207.21445);
        tour.addPath(p1);
        tour.addPath(p2);
        tour.addPath(p3);
        tour.addPath(p4);
        
        for(VertexPath vp : gp.fullPath.values()){
            Path p = vp.convertToPath(reader.getMap());
            for(Path expPath : tour.getPaths()){
                if(expPath.getDeparture() == p.getDeparture()
                        && expPath.getArrival() == p.getArrival()){
                    assertEquals(p.toString(),expPath.toString());
                }
            }
        }
    }

    /**
     * Test of hamiltonianCircuit method, of class GraphProcessor.
     */
    @Test
    public void testHamiltonianCircuit() {
        System.out.println("hamiltonianCircuit");
        XmlReader reader = new XmlReader();
        reader.readMap("src/main/resources/deliverif/app/fichiersXML2020/smallMap.xml");
        GraphProcessor instance = new GraphProcessor(reader.getMap());
        PlanningRequest pr = reader.readRequest("src/main/resources/deliverif/app/fichiersXML2020/requestsSmall1.xml");
        Vertex [] expResult = null;
        TSP1 tsp1 = instance.hamiltonianCircuit(pr);
        Graph g = instance.completeGraph(pr);
        List<Long> ordre = new ArrayList<>();
        for (Request r : pr.getRequests()) {
            ordre.add(r.getPickupAddress().getId());
            ordre.add(r.getDeliveryAddress().getId());
        }
        System.out.println("Ok");
        tsp1.searchSolution(75000, g, g.getVertexById(pr.getDepot().getAddress().getId()), ordre);
        Vertex [] result = tsp1.getSolution();
        System.out.println("Printing result");
        if (result == null) System.out.println("Result is null");
        System.out.println(result.length);
        for (Vertex v : result){
            System.out.println(v.toString());
        }
        expResult = new Vertex [3];
        
        expResult[0] = new Vertex((long) 342873658);
        expResult[1] = new Vertex((long) 208769039);
        expResult[2] = new Vertex((long) 25173820);
        
        assertEquals(Arrays.toString(expResult), Arrays.toString(result));
    }

    

    /**
     * Test of shortestPathBetweenTwoIntersections method, of class GraphProcessor.
     */
    @Test
    public void testShortestPathBetweenTwoIntersections() {
        System.out.println("shortestPathBetweenTwoIntersections");
        XmlReader reader = new XmlReader();
        reader.readMap("src/main/resources/deliverif/app/fichiersXML2020/smallMap.xml");
        GraphProcessor instance = new GraphProcessor(reader.getMap());
        PlanningRequest pr = reader.readRequest("src/main/resources/deliverif/app/fichiersXML2020/requestsSmall1.xml");
        for (Intersection i : instance.getMap().getIntersections().values()){
            System.out.println(i.toString());
        }
        Intersection v1 = instance.getMap().getIntersectionParId((long)208769039);
        Intersection v2 = instance.getMap().getIntersectionParId((long)25173820);
        Graph completeGr = instance.completeGraph(pr);
        for (String s : instance.fullPath.keySet()){
            System.out.println(s);
        }
        Path expResult = new Path();
        expResult.setDeparture(instance.getMap().getIntersectionParId((long)208769039));
        expResult.setArrival(instance.getMap().getIntersectionParId((long)25173820));
        expResult.setLength((float) 2202.1833);
        System.out.println("Before call");
        
        Path result = instance.shortestPathBetweenTwoIntersections(v1, v2);
        System.out.println("Printing path");
        System.out.println(result.toString());
        assertEquals(expResult.toString(), result.toString());
    }

    /**
     * Test get the optimal tour for a given PlanningRequest
     */
    @Test
    public void testOptimalTour() {
        System.out.println("optimalTour");

        // First let's load a map
        XmlReader reader = new XmlReader();
        boolean mapLoaded = reader.readMap("src/main/resources/deliverif/app/fichiersXML2020/smallMap.xml");
        assertTrue(mapLoaded);
        Map map = reader.getMap();
        assertNotEquals(map, null);

        // Then let's load a planning request
        PlanningRequest pr = reader.readRequest("src/main/resources/deliverif/app/fichiersXML2020/requestsSmall2.xml");
        assertNotEquals(pr, null);

        // Init the graph processor controller
        GraphProcessor instance = new GraphProcessor(map);

        // Start the algorithm to find the best path
        TourGenerator tourGenerator = instance.optimalTour(pr);
        assertNotEquals(tourGenerator, null);
        instance.startAlgo();

        // Get the computed tour
        Tour computedTour = tourGenerator.getTour();
        assertNotEquals(computedTour, null);

        // Get the tour as a list of points id
        Long[] computedTourPointsId = new Long[computedTour.getPaths().size()];
        int i = 0;
        for(Path path : computedTour.getPaths()) {
            computedTourPointsId[i] = path.getDeparture().getId();
            ++i;
        }

        // Check if the list is good
        assertArrayEquals(computedTourPointsId, new Long[]{2835339774L, 208769120L, 1679901320L, 208769457L, 25336179L});
    }

    

    /**
     * Test of changeOrder method, of class GraphProcessor.
     */
    // @Test
    public void testChangeOrder() {
        System.out.println("changeOrder");
        // First let's load a map
        XmlReader reader = new XmlReader();
        boolean mapLoaded = reader.readMap("src/main/resources/deliverif/app/fichiersXML2020/smallMap.xml");
        assertTrue(mapLoaded);
        Map map = reader.getMap();
        assertNotEquals(map, null);

        // Then let's load a planning request
        PlanningRequest pr = reader.readRequest("src/main/resources/deliverif/app/fichiersXML2020/requestsSmall2.xml");
        assertNotEquals(pr, null);

        // Init the graph processor controller
        GraphProcessor instance = new GraphProcessor(map);

        // Start the algorithm to find the best path
        TourGenerator tourGenerator = instance.optimalTour(pr);
        assertNotEquals(tourGenerator, null);
        instance.startAlgo();

        // Get the computed tour
        Tour computedTour = tourGenerator.getTour();
        assertNotEquals(computedTour, null);
        
        // Change the order of the tour
        List<Long> newOrder = new ArrayList<>();
        newOrder.add(2835339774L);
        newOrder.add(25336179L);
        newOrder.add(1679901320L);
        newOrder.add(208769120L);
        newOrder.add(208769457L);
        boolean test1 = instance.verifyNewOrder(computedTour, newOrder);
        assertFalse(test1);
        
        newOrder.remove(1);
        boolean test2 = instance.verifyNewOrder(computedTour, newOrder);
        assertFalse(test2);
        
        newOrder.add(123456789L);
        boolean test3 = instance.verifyNewOrder(computedTour, newOrder);
        assertFalse(test3);
        
        newOrder.remove(123456789L);
        newOrder.add(25336179L);
        boolean test4 = instance.verifyNewOrder(computedTour, newOrder);
        assertTrue(test4);
        
        computedTour = instance.changeOrder(computedTour, newOrder);
        // Get the tour as a list of points id
        Long[] computedTourPointsId = new Long[computedTour.getPaths().size()];
        int i = 0;
        for(Path path : computedTour.getPaths()) {
            computedTourPointsId[i] = path.getDeparture().getId();
            ++i;
        }

        // Check if the list is good
        assertArrayEquals(computedTourPointsId, new Long[]{2835339774L, 1679901320L, 208769120L, 208769457L, 25336179L});
    }

    /**
     * Test of addRequestToTour method, of class GraphProcessor.
     */
    // @Test
    public void testAddRequestToTour() {
        System.out.println("addRequestToTour");
        // First let's load a map
        XmlReader reader = new XmlReader();
        boolean mapLoaded = reader.readMap("src/main/resources/deliverif/app/fichiersXML2020/smallMap.xml");
        assertTrue(mapLoaded);
        Map map = reader.getMap();
        assertNotEquals(map, null);

        // Then let's load a planning request
        PlanningRequest pr = reader.readRequest("src/main/resources/deliverif/app/fichiersXML2020/requestsSmall2.xml");
        assertNotEquals(pr, null);

        // Init the graph processor controller
        GraphProcessor instance = new GraphProcessor(map);

        // Start the algorithm to find the best path
        TourGenerator tourGenerator = instance.optimalTour(pr);
        assertNotEquals(tourGenerator, null);
        instance.startAlgo();

        // Get the computed tour
        Tour computedTour = tourGenerator.getTour();
        assertNotEquals(computedTour, null);
        
        // Add a new request to the tour
        Request rqst = new Request();
        rqst.setPickupAddress(instance.getMap().getIntersectionParId(55475018L));
        rqst.setDeliveryAddress(instance.getMap().getIntersectionParId(26079654L));
        rqst.setPickupDuration(360);
        rqst.setDeliveryDuration(300);
        
        computedTour = instance.addRequestToTour(computedTour, rqst);
        // Get the tour as a list of points id
        Long[] computedTourPointsId = new Long[computedTour.getPaths().size()];
        int i = 0;
        for(Path path : computedTour.getPaths()) {
            computedTourPointsId[i] = path.getDeparture().getId();
            ++i;
        }

        // Check if the list is good
        assertArrayEquals(computedTourPointsId, new Long[]{2835339774L, 208769120L, 1679901320L, 208769457L, 25336179L, 55475018L, 26079654L});
    }

    /**
     * Test of removeRequestFromTour method, of class GraphProcessor.
     */
    // @Test
    public void testRemoveRequestFromTour() {
        System.out.println("removeRequestFromTour");
        // First let's load a map
        XmlReader reader = new XmlReader();
        boolean mapLoaded = reader.readMap("src/main/resources/deliverif/app/fichiersXML2020/mediumMap.xml");
        assertTrue(mapLoaded);
        Map map = reader.getMap();
        assertNotEquals(map, null);

        // Then let's load a planning request
        PlanningRequest pr = reader.readRequest("src/main/resources/deliverif/app/fichiersXML2020/requestsMedium3.xml");
        assertNotEquals(pr, null);

        // Init the graph processor controller
        GraphProcessor instance = new GraphProcessor(map);

        // Start the algorithm to find the best path
        TourGenerator tourGenerator = instance.optimalTour(pr);
        assertNotEquals(tourGenerator, null);
        instance.startAlgo();

        // Get the computed tour
        Tour computedTour = tourGenerator.getTour();
        assertNotEquals(computedTour, null);
        
        // Remove a request from the tour
        System.out.println(computedTour.getOrder());
        Request rqst = pr.findRequestByAddress(505061101L);
        computedTour = instance.removeRequestFromTour(computedTour, rqst);
        
        // Get the tour as a list of points id
        Long[] computedTourPointsId = new Long[computedTour.getPaths().size()];
        int i = 0;
        for(Path path : computedTour.getPaths()) {
            computedTourPointsId[i] = path.getDeparture().getId();
            ++i;
        }

        // Check if the list is good
        assertArrayEquals(computedTourPointsId, new Long[]{1349383079L, 26121686L, 55444018L, 191134392L, 26470086L});
    
    }

    
    /**Load text from .txt
     * @param txtPath
     * @return content in type of String
     */
    public static String readTxt(String txtPath) {
        File file = new File(txtPath);
        if(file.isFile() && file.exists()){
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                 
                StringBuilder sb = new StringBuilder();
                String text = null;
                while((text = bufferedReader.readLine()) != null){
                    text += "\n";
                    sb.append(text);
                }
                return sb.toString().substring(0, sb.toString().length()-1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
