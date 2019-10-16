package main;

import org.openjdk.jmh.annotations.Benchmark;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

public class GASeq {

    private int n;                      // problem size (in bytes)
    private int np;                     // population size
    private List<Data> p;          // population (generation 0 is randomly generated)
    private List<Double> fitness;  // fitness of all population elements
    private int maxIt;                  // max number of generations (default 1000)
    private double pbmut;               // probability to perform mutations (default 0.2)
    private Objective f;                // objective function

    // Constructor (argument names coincide with corresponding attribute names)
    public GASeq(int n, int np, int maxIt, double pbmut, Objective f) {
        try {
            // initial checks
            if (n < 0) throw new Exception("GA: problem size cannot be negative");
            this.n = n;
            if (np <= 1) throw new Exception("GA: population size cannot be equal to " + np);
            this.np = np;
            if (maxIt <= 0) throw new Exception("GA: the specified maximal number of iterations is " + maxIt);
            this.maxIt = maxIt;
            if (pbmut < 0.0 || pbmut > 1.0)
                throw new Exception("GA: the probability for mutation needs to be a real number in [0.0,1.0]");
            this.pbmut = pbmut;

            // objective function
            if (f == null) throw new Exception("GA: reference to Objective function is null");
            this.f = f;

            // generating population 0
            this.p = new ArrayList<>(this.np);
            this.fitness = new ArrayList<Double>(this.np);

            IntStream.range(0, this.np).forEach(k -> {
                Data d = new Data(this.n);
                this.p.add(d);
                try {
                    this.fitness.add(this.f.value(d));
                } catch (NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Constructor requiring less arguments (default values are used for the others)
    public GASeq(int n, int np, int maxIt, Objective f) {
        this(n, np, maxIt, 0.2, f);
    }

    // Constructor requiring less arguments (default values are used for the others)
    public GASeq(int n, int np, Objective f) {
        this(n, np, 1000, f);
    }

    // Constructor requiring less arguments (default values are used for the others)
    public GASeq(int n, Objective f) {
        this(n, n, f);
    }

    // Get the best element of the current population
    // (Data object for which the objective gives the largest value)
    public Data getBestElement()
    {
        int bestIndex = -1;
        double bestValue = Double.MIN_VALUE;
        IntStream.range(0, this.fitness.length-1).parallel().
                reduce((a,b)->intArr[a]<intArr[b]? b: a).
                ifPresent(ix -> System.out.println("Index: " + ix + ", value: " + intArr[ix]));
        for (int i = 0; i < this.np; i++)
        {
            double v = this.fitness.get(i);
            if (v > bestValue)
            {
                bestIndex = i;
                bestValue = v;
            }
        }
        return this.p.get(bestIndex);
    }

    // Select the elements of the population for reproduction
    // (works correctly only if function values are all nonnegative)
    public int select()
    {
        Random r = new Random();
        double bound = r.nextDouble();
        double sum = this.fitness.stream().reduce(0.0, Double::sum);


        int selIndex = 0;
        double psum = 0.0;
        while (selIndex < this.np - 1 && psum/sum < bound)
        {
            psum = psum + this.fitness.get(selIndex);
            selIndex++;
        }

        return selIndex;
    }

    // Verifies whether all element of the current population have the same fitness
    public boolean isStable() {
        double ref = this.fitness.get(0);
        return this.fitness.stream().allMatch(v -> ref == v);
    }

    // Performs the Genetic Algorithm by using the current population as generation 0
    public int optimize() throws NoSuchMethodException
    {
        int it = 0;
        Random r = new Random();
        while (!this.isStable() && it < this.maxIt)
        {
            // generating a new element by crossover
            int pivot = r.nextInt(8*this.n);
            int elIndex1 = this.select();
            int elIndex2 = elIndex1;
            while (elIndex2 == elIndex1)  elIndex2 = this.select();
            Data el1 = this.p.get(elIndex1);
            Data el2 = this.p.get(elIndex2);
            Data newElement = Data.crossover(el1,el2,pivot);

            // apply mutation
            if (r.nextDouble() < this.pbmut)
            {
                int bit = r.nextInt(8*this.n);
                newElement.flipBit(bit);
            }

            // select dying element (each with equal probability)
            // and replace it with the new born
            int pos = r.nextInt(this.np);
            this.p.set(pos,newElement);
            this.fitness.set(pos,this.f.value(newElement));

            // next iteration
            it++;
        }

        return it;
    }

    // toString
    public String toString()
    {
        String print = "GA\n[\n  ";
        print = print + "n = " + this.n + ", np = " + this.np + ", maxIt = " + this.maxIt + ", pbmut = " + this.pbmut + ";\n  ";
        print = print + this.f.toString();
        print = print + "\n]\n";
        return print;
    }

    public static void main(String[] args) throws NoSuchMethodException
    {
        Objective f = new Objective(0);
        GASeq gaseq = new GASeq(3,50,1000,0.2,f);
        System.out.println(gaseq);
        System.out.println("Number of iterations: " + gaseq.optimize());
        System.out.println("Solution: " + gaseq.getBestElement());
        System.out.println("and its fitness: " + f.value(gaseq.getBestElement()));
    }
}
