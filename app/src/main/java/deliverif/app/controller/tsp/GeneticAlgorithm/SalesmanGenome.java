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
 */
public class SalesmanGenome implements Comparable {

    List<Vertex> genome;
    public HashMap<String, Float> costs = new HashMap<>();
    Vertex start;
    float fitness;
    private List<Long> ordre;

    public SalesmanGenome(HashMap<String, Float> costs, Vertex start, List<Long> ordre) {
        this.costs = costs;
        this.start = start;
        this.ordre = ordre;
        this.genome = randomSalesman();
        this.fitness = this.calculateFitness();
    }

    public SalesmanGenome(List<Vertex> permutationOfStops, HashMap<String, Float> costs, Vertex start, List<Long> ordre) {
        this.genome = permutationOfStops;
        this.costs = costs;
        this.start = start;
        this.ordre = ordre;
        this.fitness = this.calculateFitness();
    }

    private List<Vertex> randomSalesman() {
        List<Vertex> result = new ArrayList<Vertex>();
        for (Edge e : start.getAdj()) {
            result.add(e.dest);
        }
        Collections.shuffle(result);
        return result;
    }

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
