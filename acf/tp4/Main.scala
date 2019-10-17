package tp4

object Main {
  def main(args : Array[String]) : Unit = {
    val exp=BinExpression("+",VariableRef("y"),BinExpression("-",IntegerValue(1),IntegerValue(2)))
    val prog=Seq(Assignement("x",IntegerValue(0)),
    					   Seq(Assignement("y",IntegerValue(1)),
              	     Seq(Read("z"),  
            					   Seq(While(BinExpression("<",VariableRef("x"),VariableRef("z")),
    					                     Seq(Assignement("x",BinExpression("+",VariableRef("x"),IntegerValue(1))),
    					    		  	             Seq(Assignement("y",BinExpression("*",VariableRef("y"),VariableRef("x"))),
    					    		  	                 Print(VariableRef("x"))))),
                    					Print(VariableRef("y"))))))
    		
                    					
    print(PrettyPrinter.stringOf(exp) +"\n************************************\n")
    println(PrettyPrinter.stringOf(prog))
    println(Interpret.eval(prog, List(5)))
    
  }
}
