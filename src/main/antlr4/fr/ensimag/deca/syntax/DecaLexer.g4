lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Deca lexer rules.
// Reserved words
ASM: 'asm';
CLASS: 'class';
EXTENDS: 'extends';
ELSE: 'else';
FALSE: 'false';
IF: 'if';
INSTANCEOF: 'instanceof';
NEW: 'new';
NULL: 'null';
READINT: 'readInt';
READFLOAT: 'readFloat';
PRINT: 'print';
PRINTLN: 'println';
PRINTX: 'printx';
PRINTLNX: 'printlnx';
PROTECTED: 'protected';
RETURN: 'return';
THIS: 'this';
TRUE: 'true';
WHILE: 'while';

// Special char
LT: '<';
GT: '>';
EQUALS: '=';
PLUS: '+';
MINUS: '-';
TIMES: '*';
SLASH: '/';
PERCENT: '%';
DOT: '.';
COMMA: ',';
OPARENT: '(';
CPARENT: ')';
OBRACE: '{';
CBRACE: '}';
EXCLAM: '!';
SEMI : ';';
EQEQ: '==';
NEQ: '!=';
GEQ: '>=';
LEQ: '<=';
AND: '&&';
OR: '||';

SPACES: ('\n'|' '|'\t') {skip();};
EOL: '\n';

// Identificators
fragment LETTER: 'a'..'z' | 'A'..'Z';
fragment DIGIT: ('0'..'9');
IDENT: (LETTER|'$'|'_')(LETTER|DIGIT|'$'|'_')*;

// Numbers
fragment POSITIVE_DIGIT: ('1'..'9');
INT: ('0'|POSITIVE_DIGIT DIGIT*);

fragment NUM: DIGIT+;
fragment SIGN: ('+'|'-'|/*e*/);
fragment EXP: ('E'|'e') SIGN NUM;
fragment DEC: NUM '.' NUM;
fragment FLOATDEC: (DEC|DEC EXP)('F'|'f'|/*e*/);
fragment DIGITHEX: ('0'..'9') | ('a'..'f') | ('A'..'F');
fragment NUMHEX: DIGITHEX+;
fragment FLOATHEX: ('0x'|'0X') NUMHEX '.' NUMHEX ('P'|'p') SIGN NUM ('F'|'f'|/*e*/);
FLOAT: FLOATDEC|FLOATHEX;

// String
fragment STRING_CAR: ~('"'|'\\'|'\n');
STRING: '"' (STRING_CAR|'\\"'|'\\\\')* '"'
   {
      String text = getText();
      text = (text).substring(1, text.length() - 1);
  //    text = (text).replace("\\\\","\\");
  //    text = (text).replace("\\\"","\"");
      setText(text);
   }
;
MULTI_LINE_STRING: '"'(STRING_CAR|EOL|'\\"'|'\\\\')*'"'
   {
      String text = getText();
      text = (text).substring(1, text.length() - 1);
   //   text = (text).replace("\\\\","\\");
   //   text = (text).replace("\\\"","\"");
      setText(text);
   }
;

// Files include
fragment FILENAME: (LETTER|DIGIT|'.'|'-'|'_')+;
INCLUDE: '#include' (' ')* '"'FILENAME'"'
   { 
   doInclude(getText());
   } 
;

// Comments
fragment ONE_LINE_COMMENT: '//'~('\n')*EOL;
fragment MULTI_LINE_COMMENT: '/*'.*?'*/';
COMMENT: (ONE_LINE_COMMENT|MULTI_LINE_COMMENT) {skip();};
