package main;
/* Genetic Algorithm (GA)
 *
 * Solves maximization optimization problems
 *
 * SE project 2019
 *
 */

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author emily Delorme
 */
public class GeneticAlgorithm {

    private final int problemSize;                      // problem size (in bytes)
    private final int populationSize;                     // population size
    private final List<Data> population;                   // population (generation 0 is randomly generated)
    private final List<Double> fitness;           // fitness of all population elements
    private final int maxIterations;                  // max number of generations (default 1000)
    private final double probabilityMutation;               // probability to perform mutations (default 0.2)
    private final Objective f;                // objective function

    // Performances Variables
    private final Random r;


    public GeneticAlgorithm(final int problemSize, final int populationSize, final int maxIterations, final double probabilityMutation, final Objective f) throws NoSuchMethodException {
        this.problemSize = problemSize;
        this.populationSize = populationSize;
        this.maxIterations = maxIterations;
        this.probabilityMutation = probabilityMutation;
        this.f = f;
        this.r = new Random();

        // generating population 0
        this.population = new ArrayList<>(populationSize);
        this.fitness = new ArrayList<>(populationSize);

        for (int i = 0; i < this.populationSize; i++) {
            final Data data = new Data(this.problemSize);
            this.population.add(data);
            this.fitness.add(this.f.value(data));
        }

    }

    /**
     * Give the best element for the current population
     *
     * @return The best element of the population according to his fitness
     */
    public Data getBestElement() {
        int bestIndex = -1;
        double bestValue = Double.MIN_VALUE;

        for (int i = 0; i < this.population.size(); i++) {
            if (!Objects.isNull(this.population.get(i)) && this.fitness.get(i) > bestValue) {
                bestIndex = i;
                bestValue = this.fitness.get(i);
            }
        }
        return this.population.get(bestIndex);
    }

    /**
     * Create a shuffled array containing all index of the population
     *
     * @return Shuffle index that correspond to the population
     */
    private int[] shuffle() {
        return IntStream.range(0, this.populationSize)
                .boxed()
                .sorted(Comparator.comparingInt(Object::hashCode))
                .mapToInt(Integer::intValue)
                .toArray();
    }

    /**
     * Select best element of the current population and delete the worst from the population
     *
     * @param shuffleIndex an array containing all index of the population
     */
    private void selectionTournament(final int[] shuffleIndex) {
        IntStream.range(0, (this.populationSize - 1) / 2).forEach(i -> {
            if (this.fitness.get(shuffleIndex[i]) >= this.fitness.get(shuffleIndex[i + 1])) {
                this.population.remove(shuffleIndex[i + 1]);
                this.fitness.remove(shuffleIndex[i + 1]);
            } else {
                this.population.remove(shuffleIndex[i]);
                this.fitness.remove(shuffleIndex[i]);
            }
        });
    }

    /**
     * Perform a crossover of the population and mutate element during the cycle
     *
     * @param shuffleIndex an array containing all index of the population
     * @throws NoSuchMethodException if the crossover don't find the correct Method
     */
    private void crossOverAndMutate(final int[] shuffleIndex) throws NoSuchMethodException {
        Data newCrossOverElement;
        for (int i = 0; i < (this.populationSize - 1) / 2; i++) {
            //apply mutation after the crossover
            newCrossOverElement = this.mutation(
                    Data.crossover(this.population.get(shuffleIndex[i]), this.population.get(shuffleIndex[i + 1]),
                            this.r.nextInt(8 * this.problemSize)));

            this.population.add(newCrossOverElement);
            this.fitness.add(this.f.value(newCrossOverElement));
        }
    }

    /**
     * Mutate an element using probabilityMutation,
     * or return non muted element.
     *
     * @param newCrossOverElement the element to mutate
     * @return Muted Data
     */
    private Data mutation(final Data newCrossOverElement) {
        if (this.r.nextDouble() < this.probabilityMutation) {
            newCrossOverElement.flipBit(this.r.nextInt(8 * this.problemSize));
        }
        return newCrossOverElement;
    }


    /**
     * Verifies whether all element of the current population have the same fitness
     * <p>
     * this function will return true really rarely
     *
     * @return true if the population is sable
     */
    private boolean isStable() {
        final double ref = this.fitness.get(0);
        return IntStream.range(1, this.populationSize).noneMatch(i -> ref != this.fitness.get(i));
    }

    /**
     * Performs the Genetic Algorithm by using the current population as generation 0
     *
     * @return the number of iteration the algorithm have done
     * @throws NoSuchMethodException if the crossover don't find the correct Method
     */
    public int optimize() throws NoSuchMethodException {
        int i;
        for (i = 0; i < this.maxIterations || this.isStable(); i++) {
            final int[] shuffleIndex = this.shuffle();
            this.selectionTournament(shuffleIndex);
            this.crossOverAndMutate(shuffleIndex);
        }
        return i;
    }

    @Override
    public String toString() {
        return "GeneticAlgorithm{" +
                "problemSize=" + this.problemSize +
                ", populationSize=" + this.populationSize +
                ", maxIterations=" + this.maxIterations +
                ", probabilityMutation=" + this.probabilityMutation +
                ", isStable=" + this.isStable() +
                '}';
    }

    /**
     * For testing purpose ONLY
     * <p>
     * Use benchmark class if you want to do stable test with 'gradle jmh',
     * this will start a benchmark of all algorithms.
     *
     * @param args
     * @throws NoSuchMethodException
     */
    public static void main(final String[] args) throws NoSuchMethodException {
        for (int i = 0; i < 10; i++) {
            System.out.println();
            System.out.println("------------Iteration " + i + "-------------");
            System.out.println();
            System.out.println("------------GeniticAlgorithm-------------");
            final Objective f = new Objective(i % 2);
            final GeneticAlgorithm ga = new GeneticAlgorithm(8, 50, 100, 0.2, f);
            System.out.println("Number of iterations: " + ga.optimize());
            System.out.println("Solution: " + ga.getBestElement());
            System.out.println("and its fitness: " + f.value(ga.getBestElement()));
            System.out.println(ga.toString());
        }

    }

}

