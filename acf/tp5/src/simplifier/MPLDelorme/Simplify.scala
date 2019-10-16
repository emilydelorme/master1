package simplifier.MPLDelorme;
//package simplifier.<IciLeNomDeVotreBinome> // Sans les chevrons < >
import library._

class Simplify extends Simplifier{
  
  def process(l:List[Symbol]): List[Symbol]=
  {
    var nl = List[Symbol]();
    var i = false;
    for (sym <- l){
      sym match{
        case Char(a) =>
          nl = nl:+sym;
          i = false
        case Qmark =>
          nl = nl:+sym
        case Star =>
          if(nl.size == 0) nl = nl:+sym
          else nl.last match {
            case Star =>
            case Qmark=>
              if(!i) nl = nl:+sym
              /*var i = nl.size-1;
              while(nl.size != 0 && (nl.last match{ case Qmark=>true case Star=>false case Char(a)=>false})){
                
              }*/
             
            case Char(a)=> nl = nl:+sym
          };
          i = true
      }
    }
    nl
  }
}



 /*for(var i = 0; i< l.size();i++){
      
    }
    
    /*if(nl.size == 0) nl = nl:+sym//premier char
          else nl.last match {
            case Star => nl = nl:+sym
            case Qmark=> nl = nl:+sym
            case Char(a)=> nl = nl:+sym
          }*/
          
          /*while(nl.size != 0 && (nl.last match{ case Qmark=>true case Star=>false case Char(a)=>false})){
                nl = nl.dropRight(1);
              }*/
          
          
    l.head match{
      case Char(a:ScalaChar) => 
        process();
      case Star =>
        
      case Qmark => l
    }
    
    l.head match{
      case Char(a:ScalaChar) => 
        l
      case Star =>
       
        
        
      case Qmark => l
    }*/

