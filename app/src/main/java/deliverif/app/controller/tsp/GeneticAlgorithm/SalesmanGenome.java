/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.tsp.GeneticAlgorithm;

import deliverif.app.model.graph.Edge;
import deliverif.app.model.graph.Vertex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author zakaria
 * Adapted from the implementation provided by Darinka Zobenica: 
 * https://github.com/Mentathiel/StackAbuseGeneticTravelingSalesman
 */
public class SalesmanGenome implements Comparable {
    // The list of stops in the order in which they should be visited.
    // A genome represents a potential solution to the problem.
    List<Vertex> genome;
    // Travel costs between two vertices, used to calculate fitness.
    public HashMap<String, Float> costs = new HashMap<>();
    Vertex start;
    float fitness;
    private List<Long> ordre;

    /**
     * Generates a random Salesman
     * @param costs maps a pair of two vertices to the travel distance between them
     * @param start the starting point, usually the warehouse
     * @param ordre the precedence constraint for visiting vertices, pickup prior to delivery
     */
    public SalesmanGenome(HashMap<String, Float> costs, Vertex start, List<Long> ordre) {
        this.costs = costs;
        this.start = start;
        this.ordre = ordre;
        this.genome = randomSalesman();
        this.fitness = this.calculateFitness();
    }
    
    /**
     * Creates a random Salesman with a user-defined genome
     * @param permutationOfStops user-defined genome
     * @param costs maps a pair of two vertices to the travel distance between them
     * @param start the starting point, usually the warehouse
     * @param ordre the precedence constraint for visiting vertices, pickup prior to delivery
     */
    public SalesmanGenome(List<Vertex> permutationOfStops, HashMap<String, Float> costs, Vertex start, List<Long> ordre) {
        this.genome = permutationOfStops;
        this.costs = costs;
        this.start = start;
        this.ordre = ordre;
        this.fitness = this.calculateFitness();
    }
    
    /**
     * Generates a random genome
     * Genomes are permutations of the list of stops to visit
     * @return a genome
     */
    private List<Vertex> randomSalesman() {
        List<Vertex> result = new ArrayList<Vertex>();
        for (Edge e : start.getAdj()) {
            result.add(e.dest);
        }
        Collections.shuffle(result);
        return result;
    }
    
    /**
     * Calculates the actual cost of taking certain path that we want to minimise
     * @return the computed cost
     */
    public float calculateFitness() {
        float fitness = 0;
        Vertex currentStop = start;

        // Calculating path cost
        boolean allowed = true;
        List<Long> pickups = new ArrayList<>();
        for (Vertex gene : genome) {
            try {
                int orderIndex = ordre.indexOf(gene.getId());
                if (orderIndex % 2 != 0) {
                    if (!pickups.contains(ordre.get(orderIndex - 1))) {
                        allowed = false;
                        break;
                    }
                } else {
                    pickups.add(gene.getId());
                }
                fitness += costs.get(currentStop.getId() + "-" + gene.getId());
            } catch (Exception e) {
                System.out.println("Error on " + currentStop.getId() + " to " + gene.getId());
                throw e;
            }
            currentStop = gene;
        }

        // We have to add going back to the starting city to complete the circle
        // the genome is missing the starting city, and indexing starts at 0, which is why we subtract 2
        fitness += costs.get(genome.get(genome.size() - 1).getId() + "-" + start.getId());

        if (!allowed) {
            fitness = Integer.MAX_VALUE;
        }

        return fitness;
    }

    public List<Vertex> getGenome() {
        return genome;
    }

    public float getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public int compareTo(Object o) {
        SalesmanGenome genome = (SalesmanGenome) o;
        if (this.fitness > genome.getFitness()) {
            return 1;
        } else if (this.fitness < genome.getFitness()) {
            return -1;
        } else {
            return 0;
        }
    }

}
