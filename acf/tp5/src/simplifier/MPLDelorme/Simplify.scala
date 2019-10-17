package simplifier.MPLDelorme
import library._

class Simplify extends Simplifier {
  
  def process(l:List[Symbol]): List[Symbol]=
  {
    var nl = List[Symbol]()
    var i = false
    for (sym <- l){
      sym match {
        case Char(a) =>
          nl = nl:+sym
          i = false
        case Qmark => nl = nl:+sym
        case Star =>
          if(nl.isEmpty) nl = nl:+sym
          else nl.last match {
            case Char(a)=> nl = nl:+sym
            case Star =>
            case Qmark=> if(!i) nl = nl:+sym
          }
          i = true
      }
    }
    nl
  }
}

