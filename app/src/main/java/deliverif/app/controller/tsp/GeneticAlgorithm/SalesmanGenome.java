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
    
    public SalesmanGenome(HashMap<String, Float> costs, Vertex start) {
        this.costs = costs;
        this.start = start;
        this.genome = randomSalesman();
        this.fitness = this.calculateFitness();
    }
    
    public SalesmanGenome(List<Vertex> permutationOfStops, HashMap<String, Float> costs, Vertex start) {
        this.genome = permutationOfStops;
        this.costs = costs;
        this.start = start;

        this.fitness = this.calculateFitness();
    }
    
    private List<Vertex> randomSalesman() {
        List<Vertex> result = new ArrayList<Vertex>();
        for (Edge e : start.getAdj()){
            result.add(e.dest);
        }
        Collections.shuffle(result);
        return result;
    } 
       
    public float calculateFitness() {
        float fitness = 0;
        Vertex currentStop = start;

        // Calculating path cost
        for (Vertex gene : genome) {
            fitness += costs.get(currentStop+"-"+gene.getId());
            currentStop = gene;
        }

        // We have to add going back to the starting city to complete the circle
        // the genome is missing the starting city, and indexing starts at 0, which is why we subtract 2

        fitness += costs.get(genome.get(genome.size()-1).getId()+"-"+start.getId());

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
        if(this.fitness > genome.getFitness())
            return 1;
        else if(this.fitness < genome.getFitness())
            return -1;
        else
            return 0;
    }
    
}
