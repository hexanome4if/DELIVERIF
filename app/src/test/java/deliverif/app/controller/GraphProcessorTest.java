/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import deliverif.app.controller.tsp.TSP1;
import deliverif.app.model.graph.Graph;
import deliverif.app.model.graph.Tour;
import deliverif.app.model.graph.Vertex;
import deliverif.app.model.map.Intersection;
import deliverif.app.model.request.Path;
import deliverif.app.model.request.PlanningRequest;
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
    
    public GraphProcessorTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }
    
    

    /**
     * Test of dijkstra method, of class GraphProcessor.
     */
    @org.junit.jupiter.api.Test
    public void testDijkstra() {
        System.out.println("dijkstra");
        Graph g = null;
        Vertex source = null;
        List<Vertex> goals = null;
        GraphProcessor instance = null;
        instance.dijkstra(g, source, goals);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of completeGraph method, of class GraphProcessor.
     */
    @org.junit.jupiter.api.Test
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
     * Test of hamiltonianCircuit method, of class GraphProcessor.
     */
    @org.junit.jupiter.api.Test
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
     * Test of dijkstraPath method, of class GraphProcessor.
     */
    @org.junit.jupiter.api.Test
    public void testDijkstraPath() {
        System.out.println("dijkstraPath");
        Graph g = null;
        Vertex source = null;
        Vertex goal = null;
        GraphProcessor instance = null;
        Path expResult = null;
        Path result = instance.dijkstraPath(g, source, goal);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of shortestPathBetweenTwoIntersections method, of class GraphProcessor.
     */
    @org.junit.jupiter.api.Test
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
    @org.junit.jupiter.api.Test
    public void testOptimalTour() {
        System.out.println("optimalTour");
        PlanningRequest pr = null;
        GraphProcessor instance = null;
        Tour expResult = null;
        Tour result = instance.optimalTour(pr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class GraphProcessor.
     */
    @org.junit.jupiter.api.Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        GraphProcessor.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
