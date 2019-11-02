package library
import scala.{Char=>ScalaChar}

trait Simplifier{
  def process(p:List[Symbol]):List[Symbol]
}

sealed trait Symbol
case class Char(a: ScalaChar) extends Symbol
case object Star extends Symbol
case object Qmark extends Symbol

//Isabelle/HOL String object and Char representation
object String {
	abstract sealed class char
	final case class
	Chara(a: Boolean, b: Boolean, c: Boolean, d: Boolean, e: Boolean, f: Boolean,
			g: Boolean, h: Boolean)
	extends char
}

object PrettyPrinter{
  def show(s:Symbol):String={
    s match{
      case Char(a) => a.toString()
      case Star => "*"
      case Qmark => "?"
    }
  }
  
  def show(l:List[Symbol]):String={
    l.map(show(_)).mkString("")
  }
}

case class ParseError(s:String) extends Exception


object Parser{ 
  def parseSymbol(c:ScalaChar):Symbol={
    c match {
      case '*' => Star
      case '?' => Qmark
      case _ => if (((('A' to 'Z') union ('a' to 'z') union ('0' to '9')).toSet+'.').contains(c)) Char(c)
                else throw ParseError("Bad symbol : "+c)
    }
  }
  def parseSymbolList(s:String):List[Symbol]={
    s.toList.map(parseSymbol(_))
  }
}