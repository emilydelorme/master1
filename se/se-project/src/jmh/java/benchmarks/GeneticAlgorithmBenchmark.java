package benchmarks;

import main.GA;
import main.GeneticAlgorithm;
import main.GeneticAlgorithmMultithread;
import main.Objective;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
@Fork(value = 0)
public class GeneticAlgorithmBenchmark {

    /**
     * Make a benchmark of single threaded genetic algorithm
     */
    @Benchmark
    public void testGeneticAlgorithmDefault() throws NoSuchMethodException {
        final Objective f = new Objective(0);
        final GA ga = new GA(512, 1000, 2500, 0.2, f);
        ga.optimize();
        ga.getBestElement();
        f.value(ga.getBestElement());
    }

    /**
     * Make a benchmark of single threaded genetic algorithm
     */
    @Benchmark
    public void testGeneticAlgorithmSingleThread() throws NoSuchMethodException {
        final Objective f = new Objective(0);
        final GeneticAlgorithm ga = new GeneticAlgorithm(512, 1000, 2500, 0.2, f);
        ga.optimize();
        ga.getBestElement();
        f.value(ga.getBestElement());
    }

    @Benchmark
    public void testGeneticAlgorithmMultiThread() throws NoSuchMethodException, InterruptedException, ExecutionException, TimeoutException {
        final Objective f = new Objective(0);
        final GeneticAlgorithmMultithread ga = new GeneticAlgorithmMultithread(512, 1000, 2500, 0.2, f, 8);
        ga.optimize();
        ga.getBestElement();
        f.value(ga.getBestElement());
    }
}