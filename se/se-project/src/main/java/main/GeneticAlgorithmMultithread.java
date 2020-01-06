package main;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GeneticAlgorithmMultithread {
    private final int problemSize;                      // problem size (in bytes)
    private final int populationSize;                     // population size
    private List<DataWithFitness> populationWithFitness;                   // population (generation 0 is randomly generated)
    private final int maxIterations;                  // max number of generations (default 1000)
    private final double probabilityMutation;               // probability to perform mutations (default 0.2)
    private final Objective f;                // objective function

    // Performances Variables
    private final Random r;
    private final ExecutorService executorService;
    private final int numberOfThread;
    private int subListCount;


    public GeneticAlgorithmMultithread(final int problemSize, final int populationSize, final int maxIterations, final double probabilityMutation, final Objective f, final int numberOfThreads) throws NoSuchMethodException {
        this.problemSize = problemSize;
        this.populationSize = populationSize;
        this.maxIterations = maxIterations;
        this.probabilityMutation = probabilityMutation;
        this.f = f;
        this.r = new Random();


        this.numberOfThread = numberOfThreads;

        // calculate how many sublist will be create for each multithreaded computation
        // and verify that sublist are bigger than 50 elements
        int populationSublist = this.populationSize;
        this.subListCount = 0;
        do {
            populationSublist -= 50;
            this.subListCount += 1;
        } while ((populationSublist > 0 && this.subListCount <= numberOfThreads));
        this.executorService = Executors.newFixedThreadPool(numberOfThreads);

        // generating population 0
        this.populationWithFitness = new ArrayList<>(populationSize);

        Data data;
        for (int i = 0; i < this.populationSize; i++) {
            data = new Data(this.problemSize);
            this.populationWithFitness.add(new DataWithFitness(data, this.f.value(data)));
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

        for (int i = 0; i < this.populationWithFitness.size(); i++) {
            if (this.populationWithFitness.get(i).getFitness() > bestValue) {
                bestIndex = i;
                bestValue = this.populationWithFitness.get(i).getFitness();
            }
        }
        return this.populationWithFitness.get(bestIndex).getData();
    }

    /**
     * Create a shuffled array containing all index of the population
     *
     * @return Shuffle index that correspond to the population
     */
    private int[] shuffle() {
        return IntStream.range(0, this.populationSize)
                .parallel()
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
    private List<DataWithFitness> selectionTournament(final int[] shuffleIndex) throws InterruptedException, ExecutionException, TimeoutException {
        //see completable future
        return IntStream.range(0, this.subListCount)
                .parallel() // Make it parallel to make convertAndGetFuture run on each thread
                .mapToObj((i) -> this.convertAndGetFuture(i, shuffleIndex))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Take the a part of the suffleIndex and call SelectionTournamentPart with a Future for async computation
     *
     * @param i            the sublist number
     * @param shuffleIndex an array containing all index of the population
     * @return a list of best element in the subList, with worst element removed
     */
    private List<DataWithFitness> convertAndGetFuture(final int i, final int[] shuffleIndex) {
        try {
            return this.selectionTournamentPart(shuffleIndex, i * (this.populationSize / this.subListCount), (i + 1) * (this.populationSize / this.subListCount))
                    .get(30, TimeUnit.SECONDS);
        } catch (final InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Give the best elements between two index. Use executorService for making the task use all of the thread allowed
     *
     * @param shuffleIndex an array containing all index of the population
     * @param start        the start index to iterate over shuffleIndex
     * @param end          the end index to iterate over shuffleIndex
     * @return A future of a list of best element in the subList, with worst element removed
     */
    private Future<List<DataWithFitness>> selectionTournamentPart(final int[] shuffleIndex, final int start, final int end) {
        return this.executorService.submit(() -> Stream.iterate(start, i -> i < end, i -> i + 2)
                .map(i -> this.selectBetter(this.populationWithFitness.get(shuffleIndex[i]), this.populationWithFitness.get(shuffleIndex[i + 1])))
                .collect(Collectors.toList()));
    }

    /**
     * Select best element according to his fitness
     *
     * @param a element 1
     * @param b element 2
     * @return the best element between a and b
     */
    private DataWithFitness selectBetter(final DataWithFitness a, final DataWithFitness b) {
        return a.getFitness() >= b.getFitness() ? a : b;
    }

    /**
     * Perform a crossover of the population and mutate element during the cycle
     *
     * @param shuffleIndex an array containing all index of the population
     * @throws NoSuchMethodException if the crossover don't find the correct Method
     */
    private void crossOverAndMutate(final int[] shuffleIndex) throws NoSuchMethodException {
        Data newCrossOverElement;
        for (int i = 0; i < (this.populationSize) / 2; i++) {
            //apply mutation after the crossover
            newCrossOverElement = this.mutation(
                    Data.crossover(this.populationWithFitness.get(shuffleIndex[i]).getData(), this.populationWithFitness.get(shuffleIndex[i + 1]).getData(),
                            this.r.nextInt(8 * this.problemSize)));

            this.populationWithFitness.add(new DataWithFitness(newCrossOverElement, this.f.value(newCrossOverElement)));
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
        final double ref = this.populationWithFitness.get(0).getFitness();
        return IntStream.range(1, this.populationSize).noneMatch(i -> ref != this.populationWithFitness.get(i).getFitness());
    }

    /**
     * Performs the Genetic Algorithm by using the current population as generation 0
     *
     * @return the number of iteration the algorithm have done
     * @throws NoSuchMethodException if the crossover don't find the correct Method
     */
    public int optimize() throws NoSuchMethodException, InterruptedException, ExecutionException, TimeoutException {
        int i;
        for (i = 0; i < this.maxIterations || this.isStable(); i++) {
            this.populationWithFitness = this.selectionTournament(this.shuffle());
            this.crossOverAndMutate(this.shuffle());
        }
        this.executorService.shutdownNow();
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
    public static void main(final String[] args) throws NoSuchMethodException, InterruptedException, ExecutionException, TimeoutException {
        for (int i = 0; i < 10; i++) {
            System.out.println();
            System.out.println("------------Iteration " + i + "-------------");
            System.out.println();
            System.out.println("------------GeniticAlgorithm-------------");
            final Objective f = new Objective(i % 2);
            final GeneticAlgorithmMultithread ga = new GeneticAlgorithmMultithread(16, 1000, 200, 0.2, f, 8);
            System.out.println("Number of iterations: " + ga.optimize());
            System.out.println("Solution: " + ga.getBestElement());
            System.out.println("and its fitness: " + f.value(ga.getBestElement()));
            System.out.println(ga.toString());
        }

    }
}
