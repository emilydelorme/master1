package TP1;

import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
*/

public class Main {
    public static void main(String[] args) {
        // Use with a manually made AST
        List<ASD.ABCObject> abcObjectsType = new ArrayList<>();
        abcObjectsType.add(new ASD.ABCObject.ObjectEntity(new ASD.Entity("poly")));

        List<ASD.ABCObject> abcObjectsAuteur = new ArrayList<>();
        abcObjectsAuteur.add(new ASD.ABCObject.ObjectEntity(new ASD.Entity("Ridoux")));
        abcObjectsAuteur.add(new ASD.ABCObject.ObjectEntity(new ASD.Entity("Ferre")));

        List<ASD.ABCObject> abcObjectsTitre = new ArrayList<>();
        abcObjectsTitre.add(new ASD.ABCObject.ObjectString("Compilation"));

        List<ASD.Body> bodiesPoly117 = new ArrayList<>();
        bodiesPoly117.add(new ASD.Body(new ASD.Entity("type"), abcObjectsType));
        bodiesPoly117.add(new ASD.Body(new ASD.Entity("auteur"), abcObjectsAuteur));
        bodiesPoly117.add(new ASD.Body(new ASD.Entity("titre"), abcObjectsTitre));

        List<ASD.ABCObject> abcObjectsType2 = new ArrayList<>();
        abcObjectsType2.add(new ASD.ABCObject.ObjectEntity(new ASD.Entity("personne")));
        abcObjectsType2.add(new ASD.ABCObject.ObjectEntity(new ASD.Entity("professeur")));

        List<ASD.Body> bodiesRidoux = new ArrayList<>();
        bodiesRidoux.add(new ASD.Body(new ASD.Entity("type"), abcObjectsType2));

        List<ASD.Triplet> triplets = new ArrayList<>();
        triplets.add(new ASD.Triplet(new ASD.Entity("poly117"), bodiesPoly117));
        triplets.add(new ASD.Triplet(new ASD.Entity("Ridoux"), bodiesRidoux));

        ASD.Document ast = new ASD.Document(triplets);
        System.out.println(ast.toNtriples());


        // Use with lexer and parser
    /*
    try {
      // Set input
      CharStream input;
      if(args.length == 0) {
        // From standard input
        input = CharStreams.fromStream(System.in);
      }
      else {
        // From file set in first argument
        input = CharStreams.fromPath(Paths.get(args[0]));
      }

      // Instantiate Lexer
      TurtleLexer lexer = new TurtleLexer(input);
      CommonTokenStream tokens = new CommonTokenStream(lexer);

      // Instantiate Parser
      TurtleParser parser = new TurtleParser(tokens);

      // Parse
      ASD.Document ast = parser.document().out;

      // Print as Ntriples
      System.out.println(ast.toNtriples());
    } catch(IOException e) {
      e.printStackTrace();
    }
    */
    }
}
