
package tp67

import utilities.Datatype._

object Analyzer {
  def safe(p: statement): Boolean = {
    val symTab: List[(List[String.char], tp67.Anyval)] = List()    
    tp67.san(p, symTab)._2
  }
}
