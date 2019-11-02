package library

import library.{String=>IsabelleString}
import library._
import scala.BigInt
import scala.{Char=>ScalaChar}


object Converter {
  // conversion from Scala String to HOL string
  def string2charList(s:String):List[IsabelleString.char]= explode(s)

 	// conversion from HOL string to Scala String (tested on letters + numbers)
  def charlist2string(l:List[IsabelleString.char]):String= {
    l match {
      case Nil => ""
      case IsabelleString.Chara(b0,b1,b2,b3,b4,b5,b6,aa)::r =>
        val binaryString= List(aa,b6,b5,b4,b3,b2,b1,b0).map(x=>if (x) "1" else "0").mkString("")
        val decimal= Integer.parseInt(binaryString,2)
        decimal.toChar+charlist2string(r)
    }
  }

  def bit_cut_integer(k: BigInt): (BigInt, Boolean) ={
		  if (k == BigInt(0)) (BigInt(0), false)
		  else {
			  val (r, s): (BigInt, BigInt) =
					  ((k: BigInt) => (l: BigInt) => if (l == 0) (BigInt(0), k) else
						  (k.abs /% l.abs)).apply(k).apply(BigInt(2));
			  ((if (BigInt(0) < k) r else (- r) - s), s == BigInt(1))
		  }
  }

  def char_of_integer(k: BigInt): IsabelleString.char ={
		  val (q0, b0): (BigInt, Boolean) = bit_cut_integer(k)
				  val (q1, b1): (BigInt, Boolean) = bit_cut_integer(q0)
				  val (q2, b2): (BigInt, Boolean) = bit_cut_integer(q1)
				  val (q3, b3): (BigInt, Boolean) = bit_cut_integer(q2)
				  val (q4, b4): (BigInt, Boolean) = bit_cut_integer(q3)
				  val (q5, b5): (BigInt, Boolean) = bit_cut_integer(q4)
				  val (q6, b6): (BigInt, Boolean) = bit_cut_integer(q5)
				  val a: (BigInt, Boolean) = bit_cut_integer(q6)
				  val (_, aa): (BigInt, Boolean) = a;
				  IsabelleString.Chara(b0, b1, b2, b3, b4, b5, b6, aa)
  }
	
  def map[A, B](f: A => B, x1: List[A]): List[B] = (f, x1) match {
    case (f, Nil) => Nil
    case (f, x :: xs) => f(x) :: map[A, B](f, xs)
  }

	def explodeList(l: List[ScalaChar]): List[IsabelleString.char] ={
    map[BigInt,IsabelleString.char](((a: BigInt) => char_of_integer(a)),
                    (l.map((c:ScalaChar) => { val k: Int = c.toInt; 
                                         if (k < 128) BigInt(k) 
                                         else sys.error("Non-ASCII character in literal") })))
	}
	
	def explode(s: String): List[IsabelleString.char] ={
	    explodeList(s.toCharArray.toList)
	}
  
  // conversion from Scala List[Char] to HOL List[String.char]
  def charList2charList(l:List[ScalaChar]):List[IsabelleString.char]= explodeList(l)
}