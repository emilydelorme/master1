parser grammar TurtleParser;

options {
  language = Java;
  tokenVocab = TurtleLexer;
}

@header {
  package TP1;
}

document returns [ASD.Document out]
  : s=statements EOF { $out = new ASD.Document($s.out); }
  ;

statements returns [List<ASD.Triplet> out]
  : { $out = new ArrayList<ASD.Triplet>(); }
  (t=triplet { $out.add($t.out); } '.')+
  ;

triplet returns [ASD.Triplet out]
  : e=entity
  { $bodies = new ArrayList<ASD.Body>(); }
  (b=body { $bodies.add($b.out); } ';')*
  b=body { $bodies.add($b.out); $out = new ASD.Triplet($e, $bodies); }
  ;

body returns [ASD.Body out]
: e=entity
{ $objects = new ArrayList<ASD.ABCObjects>(); }
(o=object { $objects.add($o.out); } ',')*
o=object { $objects.add($o.out); $out = new ASD.Body($e, $objects); }
;

object returns [ASD.ABCObjects out]
: e=entity { $out = new ASD.ABCObject.ObjectEntity($e.out); }
| '"' string '"' {  } ;

entity : '<' ident '>' ;
