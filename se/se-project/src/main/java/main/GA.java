package main;
/* Genetic Algorithm (GA)
 *
 * Solves maximization optimization problems
 *
 * This code is not optimized, you can use it as a reference to evaluate the performances of your own code:
 * ==> your code is supposed to perform *better* than this one!!
 *
 * SE project 2019
 *
 * AM
 */

import java.util.Random;
import java.util.ArrayList;

public class GA
{
   private int n;                      // problem size (in bytes)
   private int np;                     // population size
   private ArrayList<Data> p;          // population (generation 0 is randomly generated)
   private ArrayList<Double> fitness;  // fitness of all population elements
   private int maxIt;                  // max number of generations (default 1000)
   private double pbmut;               // probability to perform mutations (default 0.2)
   private Objective f;                // objective function

   // Constructor (argument names coincide with corresponding attribute names)
   public GA(int n,int np,int maxIt,double pbmut,Objective f)
   {
      try
      {
         // initial checks
         if (n < 0) throw new Exception("GA: problem size cannot be negative");
         this.n = n;
         if (np <= 1) throw new Exception("GA: population size cannot be equal to " + np);
         this.np = np;
         if (maxIt <= 0) throw new Exception("GA: the specified maximal number of iterations is " + maxIt);
         this.maxIt = maxIt;
         if (pbmut < 0.0 || pbmut > 1.0) throw new Exception("GA: the probability for mutation needs to be a real number in [0.0,1.0]");
         this.pbmut = pbmut;

         // objective function
         if (f == null) throw new Exception("GA: reference to Objective function is null");
         this.f = f;

         // generating population 0
         this.p = new ArrayList<Data> (this.np);
         this.fitness = new ArrayList<Double> (this.np);
         for (int i = 0; i < this.np; i++)
         {
            Data d = new Data(this.n);
            this.p.add(d);
            this.fitness.add(this.f.value(d));
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         System.exit(1);
      }
   }

   // Constructor requiring less arguments (default values are used for the others)
   public GA(int n,int np,int maxIt,Objective f)
   {
      this(n,np,maxIt,0.2,f);
   }

   // Constructor requiring less arguments (default values are used for the others)
   public GA(int n,int np,Objective f)
   {
      this(n,np,1000,f);
   }

   // Constructor requiring less arguments (default values are used for the others)
   public GA(int n,Objective f)
   {
      this(n,n,f);
   }

   // Get the best element of the current population 
   // (Data object for which the objective gives the largest value)
   public Data getBestElement()
   {
      int bestIndex = -1;
      double bestValue = Double.MIN_VALUE;
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
      double sum = 0.0;
      for (int i = 0; i < this.np; i++)  sum = sum + this.fitness.get(i);

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
   public boolean isStable()
   {
      boolean stable = true;
      double ref = this.fitness.get(0);
      for (int i = 1; stable && i < this.np; i++)  if (ref != this.fitness.get(i))  stable = false;
      return stable;
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

   // main -- example of use
   public static void main(String[] args) throws NoSuchMethodException
   {
      Objective f = new Objective(1);
      GA ga = new GA(3,50,1000,0.2,f);
      System.out.println(ga);
      System.out.println("Number of iterations: " + ga.optimize());
      System.out.println("Solution: " + ga.getBestElement());
      System.out.println("and its fitness: " + f.value(ga.getBestElement()));
   }
}

