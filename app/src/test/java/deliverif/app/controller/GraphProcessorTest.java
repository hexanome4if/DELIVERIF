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
        fullPath.clear();
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
        Graph result = instance.completeGraph(pr);
        Graph expResult = new Graph();
        expResult.addVertex(new Vertex(Long.valueOf(342873658)));
        expResult.addVertex(new Vertex(Long.valueOf(208769039)));
        expResult.addVertex(new Vertex(Long.valueOf(25173820)));
        expResult.addEdge(Long.valueOf(342873658), Long.valueOf(208769039), (float) 231.60083);
        expResult.addEdge(Long.valueOf(342873658), Long.valueOf(25173820), (float) 2330.1206);
        expResult.addEdge(Long.valueOf(25173820), Long.valueOf(208769039), (float) 2202.1833);
        assertEquals(expResult.toString(), result.toString());
    }

    /**
     * Test of dijkstra method, of class GraphProcessor.
     */
    @Test
    public void testDijkstra() {
        System.out.println("dijkstra");
        XmlReader reader = new XmlReader();
        reader.readMap("src/main/resources/deliverif/app/fichiersXML2020/smallMap.xml");
        GraphProcessor gp = new GraphProcessor(reader.getMap());
        Graph graphVide = new Graph();
        Graph graphExp = new Graph();
        PlanningRequest pr = reader.readRequest("src/main/resources/deliverif/app/fichiersXML2020/requestsSmall1.xml");
        Tour tour = new Tour(pr);
        
        Vertex source = new Vertex(Long.valueOf("208769457"));
        Vertex dest1 = new Vertex(Long.valueOf("1679901320"));
        Vertex dest2 = new Vertex(Long.valueOf("208769120"));
        Vertex dest3 = new Vertex(Long.valueOf("25336179"));
        Vertex dest4 = new Vertex(Long.valueOf("208769039"));
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
        gp.dijkstra(graphVide, source, goals);
        
        graphExp.addEdgeOneSide(source.getId(), dest1.getId(), (float) 411.126);
        graphExp.addEdgeOneSide(source.getId(), dest2.getId(), (float) 691.148);
        graphExp.addEdgeOneSide(source.getId(), dest3.getId(), (float) 1539.744);
        graphExp.addEdgeOneSide(source.getId(), dest4.getId(), (float) 207.214);
        assertEquals(graphVide.toString(),graphExp.toString());
        
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
     * Test of optimalTour method, of class GraphProcessor.
     */
    @Test
    public void testOptimalTour() {
        System.out.println("optimalTour");
        XmlReader reader = new XmlReader();
        reader.readMap("src/main/resources/deliverif/app/fichiersXML2020/smallMap.xml");
        GraphProcessor instance = new GraphProcessor(reader.getMap());
        PlanningRequest pr = reader.readRequest("src/main/resources/deliverif/app/fichiersXML2020/requestsSmall1.xml");
        TourGenerator expResult = null;
        TourGenerator result = instance.optimalTour(pr);
        System.out.println(result);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    

    /**
     * Test of changeOrder method, of class GraphProcessor.
     */
    @Test
    public void testChangeOrder() {
        System.out.println("changeOrder");
        Tour tour = null;
        List<Long> newOrder = null;
        GraphProcessor instance = null;
        Tour expResult = null;
        Tour result = instance.changeOrder(tour, newOrder);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addRequestToTour method, of class GraphProcessor.
     */
    @Test
    public void testAddRequestToTour() {
        System.out.println("addRequestToTour");
        Tour tour = null;
        Request rqst = null;
        GraphProcessor instance = null;
        Tour expResult = null;
        Tour result = instance.addRequestToTour(tour, rqst);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeRequestFromTour method, of class GraphProcessor.
     */
    @Test
    public void testRemoveRequestFromTour() {
        System.out.println("removeRequestFromTour");
        Tour t = null;
        Request r = null;
        GraphProcessor instance = null;
        Tour expResult = null;
        Tour result = instance.removeRequestFromTour(t, r);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
