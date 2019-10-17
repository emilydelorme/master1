package main

import scala.io.StdIn.readLine
import simplifier.MPLDelorme._
import library._

object Application extends App {
  val simp= new Simplify()
  while(true){
    print("Pattern: ")
    val p= readLine
    val p2= Parser.parseSymbolList(p)
    val ps1= simp.process(p2)
    println("Simplified: "+PrettyPrinter.show(ps1))
  }
}