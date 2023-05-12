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
// -----------------special words --------------

PRINTLN : 'println';
PRINT : 'print';



// ------------------ Others -----------------
fragment LETTER: 'a' .. 'z' | 'A' .. 'Z';

fragment DIGIT: '0' .. '9';

EOL: '\n';

SPACES: ' ' {skip(); };

IDENT: (LETTER | '$' | '_') (LETTER | DIGIT | '$' | '_')*;

fragment POSITIVE_DIGIT: '1' .. '9';

INT: '0' | POSITIVE_DIGIT DIGIT*;

//------- FLOAT --------

fragment NUM : DIGIT+;
fragment SIGN : '+' | '-' | /* epsilon */;
fragment EXP : ('E' | 'e') SIGN NUM;
fragment DEC : NUM '.' NUM;
fragment FLOATDEC : (DEC | DEC EXP) ('F' | 'f' | /* epsilon */ );
fragment DIGITHEX : '0' .. '9' | 'A' .. 'F' | 'a' .. 'f';
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' | /* epsilon */);
FLOAT : FLOATDEC | FLOATHEX;

//--------------------- special cars ---------------------------

OPARENT: '(';

CPARENT: ')';

OBRACE: '{';

CBRACE: '}';

SEMI: ';';
COMMA: ',';
EQUALS : '=';

// String


STRING_CAR: ~["\\\r\n];
STRING: '"' (STRING_CAR | '\\' STRING_CAR)* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';

COMMENT: '//' (~('\n'))* { skip(); };

MULTI_LINE_COMMENT : '/*' (STRING_CAR | EOL)* '*/' { skip(); };

// Deca lexer rules.
DUMMY_TOKEN: .; // A FAIRE : Règle bidon qui reconnait tous les caractères.
                // A FAIRE : Il faut la supprimer et la remplacer par les vraies règles.
