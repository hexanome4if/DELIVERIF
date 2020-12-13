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
    }
    
    
    /**
     * Test of completeGraph method, of class GraphProcessor.
     */
    @Test
    public void testCompleteGraph() {
        System.out.println("completeGraph");
        PlanningRequest pr = null;
        GraphProcessor instance = null;
        Graph expResult = null;
        Graph result = instance.completeGraph(pr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dijkstra method, of class GraphProcessor.
     */
    @Test
    public void testDijkstra() {
        System.out.println("dijkstra");
        Graph completeGraph = null;
        Vertex source = null;
        List<Vertex> goals = null;
        GraphProcessor instance = null;
        instance.dijkstra(completeGraph, source, goals);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hamiltonianCircuit method, of class GraphProcessor.
     */
    @Test
    public void testHamiltonianCircuit() {
        System.out.println("hamiltonianCircuit");
        PlanningRequest pr = null;
        GraphProcessor instance = null;
        TSP1 expResult = null;
        TSP1 result = instance.hamiltonianCircuit(pr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hamiltonianCircuit2 method, of class GraphProcessor.
     */
    @Test
    public void testHamiltonianCircuit2() {
        System.out.println("hamiltonianCircuit2");
        PlanningRequest pr = null;
        GraphProcessor instance = null;
        Vertex[] expResult = null;
        Vertex[] result = instance.hamiltonianCircuit2(pr);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of shortestPathBetweenTwoIntersections method, of class GraphProcessor.
     */
    @Test
    public void testShortestPathBetweenTwoIntersections() {
        System.out.println("shortestPathBetweenTwoIntersections");
        Intersection v1 = null;
        Intersection v2 = null;
        GraphProcessor instance = null;
        Path expResult = null;
        Path result = instance.shortestPathBetweenTwoIntersections(v1, v2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of optimalTour method, of class GraphProcessor.
     */
    @Test
    public void testOptimalTour() {
        System.out.println("optimalTour");
        PlanningRequest pr = null;
        GraphProcessor instance = null;
        TourGenerator expResult = null;
        TourGenerator result = instance.optimalTour(pr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of startAlgo method, of class GraphProcessor.
     */
    @Test
    public void testStartAlgo() {
        System.out.println("startAlgo");
        GraphProcessor instance = null;
        instance.startAlgo();
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
     * Test of getNewPath method, of class GraphProcessor.
     */
    @Test
    public void testGetNewPath() {
        System.out.println("getNewPath");
        Long idStart = null;
        Long idStop = null;
        GraphProcessor instance = null;
        Path expResult = null;
        Path result = instance.getNewPath(idStart, idStop);
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

    /**
     * Test of main method, of class GraphProcessor.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        GraphProcessor.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
