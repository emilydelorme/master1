package tp67

object HOL {

trait equal[A] {
  val `HOL.equal`: (A, A) => Boolean
}
def equal[A](a: A, b: A)(implicit A: equal[A]): Boolean = A.`HOL.equal`(a, b)
object equal {
  implicit def `String.equal_char`: equal[String.char] = new equal[String.char]
    {
    val `HOL.equal` = (a: String.char, b: String.char) =>
      String.equal_chara(a, b)
  }
}

def eq[A : equal](a: A, b: A): Boolean = equal[A](a, b)

} /* object HOL */

object Num {

abstract sealed class num
final case class One() extends num
final case class Bit0(a: num) extends num
final case class Bit1(a: num) extends num

} /* object Num */

object Code_Numeral {

def integer_of_int(x0: Int.int): BigInt = x0 match {
  case Int.int_of_integer(k) => k
}

} /* object Code_Numeral */

object Int {

abstract sealed class int
final case class int_of_integer(a: BigInt) extends int

def one_int: int = int_of_integer(BigInt(1))

def plus_int(k: int, l: int): int =
  int_of_integer(Code_Numeral.integer_of_int(k) +
                   Code_Numeral.integer_of_int(l))

def zero_int: int = int_of_integer(BigInt(0))

def equal_int(k: int, l: int): Boolean =
  Code_Numeral.integer_of_int(k) == Code_Numeral.integer_of_int(l)

def minus_int(k: int, l: int): int =
  int_of_integer(Code_Numeral.integer_of_int(k) -
                   Code_Numeral.integer_of_int(l))

def uminus_int(k: int): int =
  int_of_integer((- (Code_Numeral.integer_of_int(k))))

} /* object Int */

object Product_Type {

def equal_bool(p: Boolean, pa: Boolean): Boolean = (p, pa) match {
  case (p, true) => p
  case (p, false) => ! p
  case (true, p) => p
  case (false, p) => ! p
}

} /* object Product_Type */

object String {

abstract sealed class char
final case class
  Chara(a: Boolean, b: Boolean, c: Boolean, d: Boolean, e: Boolean, f: Boolean,
         g: Boolean, h: Boolean)
  extends char

def equal_chara(x0: char, x1: char): Boolean = (x0, x1) match {
  case (Chara(x1, x2, x3, x4, x5, x6, x7, x8),
         Chara(y1, y2, y3, y4, y5, y6, y7, y8))
    => Product_Type.equal_bool(x1, y1) &&
         (Product_Type.equal_bool(x2, y2) &&
           (Product_Type.equal_bool(x3, y3) &&
             (Product_Type.equal_bool(x4, y4) &&
               (Product_Type.equal_bool(x5, y5) &&
                 (Product_Type.equal_bool(x6, y6) &&
                   (Product_Type.equal_bool(x7, y7) &&
                     Product_Type.equal_bool(x8, y8)))))))
}

} /* object String */

object Lista {

def equal_list[A : HOL.equal](x0: List[A], x1: List[A]): Boolean = (x0, x1)
  match {
  case (Nil, x21 :: x22) => false
  case (x21 :: x22, Nil) => false
  case (x21 :: x22, y21 :: y22) =>
    HOL.eq[A](x21, y21) && equal_list[A](x22, y22)
  case (Nil, Nil) => true
}

} /* object Lista */

object tp67 {

abstract sealed class Anyval
final case class Myint(a: Int.int) extends Anyval
final case class Any() extends Anyval

abstract sealed class Anycond
final case class Mybool(a: Boolean) extends Anycond
final case class Anycond() extends Anycond

def assoc2(s: List[String.char], x1: List[(List[String.char], Anyval)]): Anyval
  =
  (s, x1) match {
  case (s, Nil) => Myint(Int.uminus_int(Int.one_int))
  case (s, (x, y) :: next) =>
    (if (Lista.equal_list[String.char](s, x)) y else assoc2(s, next))
}

def evale2(x0: expression, e: List[(List[String.char], Anyval)]): Anyval =
  (x0, e) match {
  case (Constant(s), e) => Myint(s)
  case (Variable(s), e) => (assoc2(s, e) match {
                              case Myint(a) => Myint(a)
                              case Any() => Any()
                            })
  case (Sum(e1, e2), e) =>
    ((evale2(e1, e), evale2(e2, e)) match {
       case (Myint(int), Myint(b)) => Myint(Int.plus_int(int, b))
       case (Myint(_), Any()) => Any()
       case (Any(), _) => Any()
     })
  case (Sub(e1, e2), e) =>
    ((evale2(e1, e), evale2(e2, e)) match {
       case (Myint(int), Myint(b)) => Myint(Int.minus_int(int, b))
       case (Myint(_), Any()) => Any()
       case (Any(), _) => Any()
     })
}

def evalc2(x0: condition, e: List[(List[String.char], Anyval)]):
      (List[(List[String.char], Anyval)], Anycond)
  =
  (x0, e) match {
  case (Eq(Variable(e1), e2), e) =>
    ((evale2(Variable(e1), e), evale2(e2, e)) match {
       case (Myint(int), Myint(b)) =>
         ((e1, Myint(int)) :: e, Mybool(Int.equal_int(int, b)))
       case (Myint(_), Any()) => (e, Anycond())
       case (Any(), Myint(d)) => ((e1, Myint(d)) :: e, Anycond())
       case (Any(), Any()) => (e, Anycond())
     })
  case (Eq(Constant(v), e2), e) =>
    ((evale2(Constant(v), e), evale2(e2, e)) match {
       case (Myint(int), Myint(b)) => (e, Mybool(Int.equal_int(int, b)))
       case (Myint(_), Any()) => (e, Anycond())
       case (Any(), _) => (e, Anycond())
     })
  case (Eq(Sum(v, va), e2), e) =>
    ((evale2(Sum(v, va), e), evale2(e2, e)) match {
       case (Myint(int), Myint(b)) => (e, Mybool(Int.equal_int(int, b)))
       case (Myint(_), Any()) => (e, Anycond())
       case (Any(), _) => (e, Anycond())
     })
  case (Eq(Sub(v, va), e2), e) =>
    ((evale2(Sub(v, va), e), evale2(e2, e)) match {
       case (Myint(int), Myint(b)) => (e, Mybool(Int.equal_int(int, b)))
       case (Myint(_), Any()) => (e, Anycond())
       case (Any(), _) => (e, Anycond())
     })
}

def san(x0: statement, t: List[(List[String.char], Anyval)]):
      (List[(List[String.char], Anyval)], Boolean)
  =
  (x0, t) match {
  case (Aff(s, e), t) => ((s, evale2(e, t)) :: t, true)
  case (Read(s), t) => ((s, Any()) :: t, true)
  case (If(c, s1, s2), t) => (evalc2(c, t) match {
                                case (_, Mybool(true)) => san(s1, t)
                                case (_, Mybool(false)) => san(s2, t)
                                case (_, Anycond()) => (t, false)
                              })
  case (Seq(s1, s2), t) =>
    {
      val (t1, b1): (List[(List[String.char], Anyval)], Boolean) = san(s1, t)
      val (t2, b2): (List[(List[String.char], Anyval)], Boolean) = san(s2, t1);
      (t2, b1 && b2)
    }
  case (Exec(e), t) =>
    (evale2(e, t) match {
       case Myint(x) => (t, ! (Int.equal_int(x, Int.zero_int)))
       case Any() => (t, false)
     })
  case (Print(v), t) => (t, true)
  case (Skip, t) => (t, true)
}

} /* object tp67 */
