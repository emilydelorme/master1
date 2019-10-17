

package tp4

object Interpret {
  var table: Map[String, Int] = Map()
  var inList: List[Int] = List()
 
  def eval(p:Statement, inList:List[Int]): List[Int] = {
    this.inList = inList
    eval(p)
  }
  
  def eval(p:Statement): List[Int] = {
    var rtn: List[Int] = List()
    p match {
      case Seq(s1, s2) => eval(s1) ++ eval(s2)
      case If(c,s1,s2) => if (eval(c)==1) eval(s1) else eval(s2) 
      case While(c, s) => while (eval(c)==1) { rtn = eval(s) ++ rtn }; rtn
      case Assignement(v, e) => table += (v -> eval(e)); List()
      case Print(e) => List(eval(e))
      case Read(s) => table += (s -> (if(inList.isEmpty) -1 else inList.head)); inList = inList.drop(1); List()
    }
  }
  
  def eval(e:Expression): Int = {
     e match {
      case IntegerValue(i) => i
      case VariableRef(v) => table.get(v) match {
        case None => -1
        case Some(x) => x
      }
      case BinExpression(op, e1, e2) => op match  {
                                                    case "+" => eval(e1) + eval(e2)
                                                    case "-" => eval(e1) - eval(e2)
                                                    case "*" => eval(e1) * eval(e2)
                                                    case "/" => eval(e1) / eval(e2)
                                                    case "<" => if (eval(e1) < eval(e2)) 1 else 0
                                                    case "=" => if (eval(e1) == eval(e2)) 1 else 0
                                                    case ">" => if (eval(e1) > eval(e2)) 1 else 0
      }
    }
  }
}