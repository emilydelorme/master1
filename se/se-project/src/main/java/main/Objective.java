package main;
/* Objective functions
 *
 * for SE project 2019
 *
 * AM
 */

import java.util.ArrayList;
import java.lang.reflect.*;

public class Objective
{
   private int id;
   private String name;
   private String methodName;
   private Double lastValue;

   // Private constructor
   private Objective()
   {
      this.id = -1;
      this.name = "";
      this.methodName = "";
      this.lastValue = null;
   }

   // Constructor: creates an Objective object from a given id
   // id = 0 ==> objective "BitSum", performs the sum of the bits in the Data object
   // id = 1 ==> objective "SubSetBitSum", computes the absolute value of (number of zeros - number of ones)
   public Objective(int id)
   {
      try
      {
         if (id == 0)
         {
            this.id = id;
            this.name = "BitSum";
            this.methodName = "obj0";
            this.lastValue = null;
         }
         else if (id == 1)
         {
            this.id = id;
            this.name = "SubSetBitSum";
            this.methodName = "obj1";
            this.lastValue = null;
         }
         else throw new Exception("Objective: id " + id + " unknown");
      }
      catch (Exception e)
      {
         e.printStackTrace();
         System.exit(1);
      }
   }

   // Method implementing Objective related to id = 0
   private double obj0(Data D)
   {
      double count = 0.0;
      for (int k = 0; k < D.numberOfBits(); k++)  count = count + D.getBit(k);
      return count;
   }

   // Method implementing Objective related to id = 1
   private double obj1(Data D)
   {
      double count = 0.0;

      for (int k = 0; k < D.numberOfBits(); k++)
      {
         if (D.getBit(k) == 0)
            count = count + 1.0;
         else
            count = count - 1.0;
      }
      if (count < 0.0)  count = -count;

      return count;
   }

   // General method evaluating the value of the selected objective for the Data object D
   public double value(Data D) throws NoSuchMethodException
   {
      Objective O = new Objective();
      Method method = O.getClass().getDeclaredMethod(this.methodName,Data.class);
      try
      {
         this.lastValue = (Double) method.invoke(this,D);
      }
      catch (Exception e)
      {
         e.printStackTrace();
         System.exit(1);
      }
      return (double) this.lastValue;
   }

   // toString
   public String toString()
   {
      String print = this.name + " (id = " + this.id;
      if (this.lastValue != null)  print = print + ", last computed value = " + this.lastValue;
      print = print + ")";
      return print;
   }
}

