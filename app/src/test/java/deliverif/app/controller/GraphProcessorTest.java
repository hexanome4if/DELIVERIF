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
import deliverif.app.model.graph.VertexPath;
import deliverif.app.model.request.PlanningRequest;
import java.util.HashMap;
import java.util.List;
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
        HashMap<String, VertexPath> fullPath = new HashMap<>();
        instance.dijkstra(g, source, goals, fullPath);
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
        HashMap<String, VertexPath> fullPath = new HashMap<>();
        Graph result = instance.completeGraph(pr, fullPath);
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
        HashMap<String, VertexPath> fullPath = new HashMap<>();
        TSP1 result = instance.hamiltonianCircuit(pr, fullPath);
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

}
