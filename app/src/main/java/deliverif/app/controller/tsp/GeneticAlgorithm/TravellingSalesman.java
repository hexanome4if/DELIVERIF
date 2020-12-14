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
 * Adapted from the implementation provided by Darinka Zobenica: https://github.com/Mentathiel/StackAbuseGeneticTravelingSalesman

 */
public class TravellingSalesman {

    private int generationSize;
    private int genomeSize;
    private int reproductionSize;
    private int maxIterations;
    private float mutationRate;
    private HashMap<String, Float> costs;
    private Vertex start;
    private float targetFitness;
    private int tournamentSize;
    private SelectionType selectionType;
    private List<Long> ordre;

    public enum SelectionType {
        TOURNAMENT,
        ROULETTE
    }

    public TravellingSalesman(SelectionType selectionType, Graph g, Vertex start, List<Long> ordre, int maxIterations) {
        this.selectionType = selectionType;
        this.ordre = ordre;
        this.costs = new HashMap<>();
        onInit(g, start);
        this.start = start;
        this.targetFitness = targetFitness;
        this.genomeSize = start.getAdj().size();

        generationSize = 1000;
        reproductionSize = 750;
        this.maxIterations = maxIterations;
        mutationRate = 0.1f;
        tournamentSize = 40;
    }
    /**
     * A method to call on initialisation to fill the cost map.
     * @param g the complete graph from previous processing (see GraphProcessor)
     * @param start the starting point, usually the warehouse
     */
    protected void onInit(Graph g, Vertex start) {
        for (Vertex v : g.getVertexMap().values()) {
            for (Edge e : v.getAdj()) {
                if (e.dest.getId() == start.getId()) {
                    System.out.println("Add: " + v.getId() + "-" + "0");
                    costs.put(v.getId() + "-" + e.dest.getId(), e.cost);
                } else {
                    System.out.println("Add: " + v.getId() + "-" + e.dest.getId());
                    costs.put(v.getId() + "-" + e.dest.getId(), e.cost);
                }
            }
        }
    }
    /**
     * Generates a first solution candidate
     * @return the random population to start the algorithm with
     */
    public List<SalesmanGenome> initialPopulation() {
        List<SalesmanGenome> population = new ArrayList<>();
        for (int i = 0; i < generationSize; i++) {
            population.add(new SalesmanGenome(costs, start, ordre));
        }
        return population;
    }
    
    /**
     * Select reproductionSize genomes based on the method predefined in the selectionType attribute
     * @param population a list of genomes
     * @return a list of selected genomes
     */
    public List<SalesmanGenome> selection(List<SalesmanGenome> population) {
        List<SalesmanGenome> selected = new ArrayList<>();
        SalesmanGenome winner;
        for (int i = 0; i < reproductionSize; i++) {
            if (selectionType == SelectionType.ROULETTE) {
                selected.add(rouletteSelection(population));
            } else if (selectionType == SelectionType.TOURNAMENT) {
                selected.add(tournamentSelection(population));
            }
        }

        return selected;
    }

    /**
     * Implements the roulette selection approach
     * @param population a generation of genomes
     * @return 
     */
    public SalesmanGenome rouletteSelection(List<SalesmanGenome> population) {
        float totalFitness = 0;
        for (SalesmanGenome gen : population) {
            totalFitness += gen.getFitness();
        }

        // We pick a random value - a point on our roulette wheel
        Random random = new Random();
        int selectedValue = random.nextInt((int) totalFitness);

        // Because we're doing minimization, we need to use reciprocal
        // value so the probability of selecting a genome would be
        // inversely proportional to its fitness - the smaller the fitness
        // the higher the probability
        float recValue = (float) 1 / selectedValue;

        // We add up values until we reach out recValue, and we pick the
        // genome that crossed the threshold
        float currentSum = 0;
        for (SalesmanGenome genome : population) {
            currentSum += (float) 1 / genome.getFitness();
            if (currentSum >= recValue) {
                return genome;
            }
        }

        // In case the return didn't happen in the loop above, we just
        // select at random
        int selectRandom = random.nextInt(generationSize);
        return population.get(selectRandom);
    }
    /**
     * A helper function to pick n random elements from the population
     * so we could enter them into a tournament
     * @param <E> Generic output type
     * @param list list of generic type
     * @param n number of elements in list
     * @return a list of randomly picked elements
     */
    public static <E> List<E> pickNRandomElements(List<E> list, int n) {
        Random r = new Random();
        int length = list.size();

        if (length < n) {
            return null;
        }

        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i, r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    
    /**
     * A simple implementation of the deterministic tournament - the best genome
     * always wins
     * @param population a generation of genomes
     * @return a genome instance (candidate solution)
     */
    public SalesmanGenome tournamentSelection(List<SalesmanGenome> population) {
        List<SalesmanGenome> selected = pickNRandomElements(population, tournamentSize);
        return Collections.min(selected);
    }

    /**
     * The technique used is called Partially Mapped Crossover (PMX)
     * PMX randomly picks one crossover point, but unlike one-point crossover,
     *  it doesn't just swap elements from two parents, but instead swaps the elements within them. 
     * @param parents a list of genomes to participate in reproduction
     * @return list of genomes after crossover
     */
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
        children.add(new SalesmanGenome(parent1Genome, costs, start, ordre));
        parent1Genome = parents.get(0).getGenome(); // Reseting the edited parent

        // Creating child 2
        for (int i = breakpoint; i < genomeSize; i++) {
            Vertex newVal = parent1Genome.get(i);
            Collections.swap(parent2Genome, parent2Genome.indexOf(newVal), i);
        }
        children.add(new SalesmanGenome(parent2Genome, costs, start, ordre));

        return children;
    }

    /**
     * If we pass a probability check we mutate by swapping two cities in the genome. 
     * Otherwise, we just return the original genome
     * @param salesman a genome to mutate
     * @return either the original genome or after swapping
     */
    public SalesmanGenome mutate(SalesmanGenome salesman) {
        Random random = new Random();
        float mutate = random.nextFloat();
        if (mutate < mutationRate) {
            List<Vertex> genome = salesman.getGenome();
            Collections.swap(genome, random.nextInt(genomeSize), random.nextInt(genomeSize));
            return new SalesmanGenome(genome, costs, start, ordre);
        }
        return salesman;
    }

    /**
     * This is generational algorithm, so we make an entirely new population of children
     * @param population a generation of genomes
     * @return a new population of children
     */
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

    /**
     * This method orchestrates the computations and  terminates if 
     * the number of generations has reached maxIterations
     * @return the best solution found by the algorithm
     */
    public SalesmanGenome optimise() {
        List<SalesmanGenome> population = initialPopulation();
        SalesmanGenome globalBestGenome = population.get(0);
        for (int i = 0; i < maxIterations; i++) {
            List<SalesmanGenome> selected = selection(population);
            population = createGeneration(selected);
            SalesmanGenome popBest = Collections.min(population);
            if (popBest.getFitness() < globalBestGenome.getFitness()) {
                globalBestGenome = popBest;
            }
        }
        globalBestGenome.getGenome().add(0, start);
        return globalBestGenome;
    }

    public void printGeneration(List<SalesmanGenome> generation) {
        for (SalesmanGenome genome : generation) {
            System.out.println(genome);
        }
    }
}
