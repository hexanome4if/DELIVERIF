/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.tsp.GeneticAlgorithm;

import deliverif.app.model.graph.Edge;
import deliverif.app.model.graph.Graph;
import deliverif.app.model.graph.Vertex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author zakaria
 */
public class TravellingSalesman {
    private int generationSize;
    private int genomeSize;
    private int numberOfStops;
    private int reproductionSize;
    private int maxIterations;
    private float mutationRate;
    private HashMap<String, Float> costs;
    private Vertex start;
    private int targetFitness;
    private int tournamentSize;
    private SelectionType selectionType;
    public enum SelectionType {
        TOURNAMENT,
        ROULETTE
    }
    
    
    public TravellingSalesman (int numberOfStops, SelectionType selectionType, Graph g, Vertex start, int targetFitness){
        this.numberOfStops = numberOfStops;
        this.genomeSize = numberOfStops-1;
        this.selectionType = selectionType;
        onInit(g,start);
        this.start = start;
        this.targetFitness = targetFitness;

        generationSize = 5000;
        reproductionSize = 200;
        maxIterations = 1000;
        mutationRate = 0.1f;
        tournamentSize = 40;
    }
    
    
    protected void onInit(Graph g, Vertex start) {
        for (Vertex v : g.getVertexMap().values()) {
            for (Edge e : v.getAdj()) {
                if (e.dest.getId() == start.getId()) {
                    System.out.println("Add: " + v.getId() + "-" + "0");
                    costs.put(v.getId() + "-" + "0", e.cost);
                } else {
                    System.out.println("Add: " + v.getId() + "-" + e.dest.getId());
                    costs.put(v.getId() + "-" + e.dest.getId(), e.cost);
                }
            }
        }
    }
    
    public List<SalesmanGenome> initialPopulation(){
       List<SalesmanGenome> population = new ArrayList<>();
       for(int i=0; i<generationSize; i++){
           population.add(new SalesmanGenome(numberOfStops, costs, start));
       }
       return population;
    }
    
    public List<SalesmanGenome> selection(List<SalesmanGenome> population) {
        List<SalesmanGenome> selected = new ArrayList<>();
        SalesmanGenome winner;
        for (int i=0; i < reproductionSize; i++) {
            if (selectionType == SelectionType.ROULETTE) {
                selected.add(rouletteSelection(population));
            }
            else if (selectionType == SelectionType.TOURNAMENT) {
                selected.add(tournamentSelection(population));
            }
        }

        return selected;
    }
    
    public SalesmanGenome rouletteSelection(List<SalesmanGenome> population) {
        int totalFitness = population.stream().map(SalesmanGenome::getFitness).mapToInt(Integer::intValue).sum();

        // We pick a random value - a point on our roulette wheel
        Random random = new Random();
        int selectedValue = random.nextInt(totalFitness);

        // Because we're doing minimization, we need to use reciprocal
        // value so the probability of selecting a genome would be
        // inversely proportional to its fitness - the smaller the fitness
        // the higher the probability
        float recValue = (float) 1/selectedValue;

        // We add up values until we reach out recValue, and we pick the
        // genome that crossed the threshold
        float currentSum = 0;
        for (SalesmanGenome genome : population) {
            currentSum += (float) 1/genome.getFitness();
            if (currentSum >= recValue) {
                return genome;
            }
        }

        // In case the return didn't happen in the loop above, we just
        // select at random
        int selectRandom = random.nextInt(generationSize);
        return population.get(selectRandom);
    }
    
    // A helper function to pick n random elements from the population
    // so we could enter them into a tournament
    public static <E> List<E> pickNRandomElements(List<E> list, int n) {
        Random r = new Random();
        int length = list.size();

        if (length < n) return null;

        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i , r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    // A simple implementation of the deterministic tournament - the best genome
    // always wins
    public SalesmanGenome tournamentSelection(List<SalesmanGenome> population) {
        List<SalesmanGenome> selected = pickNRandomElements(population, tournamentSize);
        return Collections.min(selected);
    }
    
    public List<SalesmanGenome> crossover(List<SalesmanGenome> parents) {
        // Housekeeping
        Random random = new Random();
        int breakpoint = random.nextInt(genomeSize);
        List<SalesmanGenome> children = new ArrayList<>();

        // Copy parental genomes - we copy so we wouldn't modify in case they were
        // chosen to participate in crossover multiple times
        List<Vertex> parent1Genome = new ArrayList<>(parents.get(0).getGenome());
        List<Vertex> parent2Genome = new ArrayList<>(parents.get(1).getGenome());

        // Creating child 1
        for (int i = 0; i < breakpoint; i++) {
            Vertex newVal;
            newVal = parent2Genome.get(i);
            Collections.swap(parent1Genome, parent1Genome.indexOf(newVal), i);
        }
        children.add(new SalesmanGenome(parent1Genome, numberOfStops, costs, start));
        parent1Genome = parents.get(0).getGenome(); // Reseting the edited parent

        // Creating child 2
        for (int i = breakpoint; i < genomeSize; i++) {
            Vertex newVal = parent1Genome.get(i);
            Collections.swap(parent2Genome, parent2Genome.indexOf(newVal), i);
        }
        children.add(new SalesmanGenome(parent2Genome, numberOfStops, costs, start));

        return children;
    }
    
    public SalesmanGenome mutate(SalesmanGenome salesman) {
        Random random = new Random();
        float mutate = random.nextFloat();
        if (mutate < mutationRate) {
            List<Vertex> genome = salesman.getGenome();
            Collections.swap(genome, random.nextInt(genomeSize), random.nextInt(genomeSize));
            return new SalesmanGenome(genome, numberOfStops, costs, start);
        }
        return salesman;
    }
    
    public List<SalesmanGenome> createGeneration(List<SalesmanGenome> population) {
        List<SalesmanGenome> generation = new ArrayList<>();
        int currentGenerationSize = 0;
        while (currentGenerationSize < generationSize) {
            List<SalesmanGenome> parents = pickNRandomElements(population, 2);
            List<SalesmanGenome> children = crossover(parents);
            children.set(0, mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));
            generation.addAll(children);
            currentGenerationSize += 2;
        }
        return generation;
    }
    
    public SalesmanGenome optimise() {
        List<SalesmanGenome> population = initialPopulation();
        SalesmanGenome globalBestGenome = population.get(0);
        for (int i = 0; i < maxIterations; i++) {
            List<SalesmanGenome> selected = selection(population);
            population = createGeneration(selected);
            globalBestGenome = Collections.min(population);
            if (globalBestGenome.getFitness() < targetFitness)
                break;
        }
        return globalBestGenome;
    }
    
    public void printGeneration(List<SalesmanGenome> generation ){
        for( SalesmanGenome genome : generation){
            System.out.println(genome);
        }
    }
}
