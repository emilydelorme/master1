

package tp4

object PrettyPrinter {
  def stringOf(e:Expression):String = {
    e match {
      case IntegerValue(i) => i.toString
      case VariableRef(v) => v
      case BinExpression(op, e1, e2) => "(" + stringOf(e1) + " " + op + " " + stringOf(e2) + ")"
    }
  }
  def stringOf(s:Statement):String = {
    s match {
      case Seq(s1, s2) => stringOf(s1) + "\n" + stringOf(s2)
      case If(c,s1,s2) => "if (" + stringOf(c) + ")\n{\n" + stringOf(s1) + "\n}\nelse\n{\n" + stringOf(s2) + "\n}"
      case While(c, s) => "while (" + stringOf(c) + ")\n{\n" + stringOf(s) + "\n}"
      case Assignement(v, e) => v + ":= " + stringOf(e)
      case Print(e) => "print(" + stringOf(e) + ")"
      case Read(s) => "read(" + s + ")"
    }
  }
}