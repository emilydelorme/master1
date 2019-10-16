lexer grammar TurtleLexer;

options {
  language = Java;
}

@header {
  package TP1;
}

// ignore whitespaces
WS : (' '|'\n'|'\t'|'\r'|'\u000C')+ -> skip
   ;

fragment ASCII  : ~('\n'|'"'|'<'|'>');

ident : ('a'..'z'|'A'..'Z')('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')*;

string : (ASCII)+;
