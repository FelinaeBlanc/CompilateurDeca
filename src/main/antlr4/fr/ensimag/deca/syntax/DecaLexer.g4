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
LETTER : [a-zA-Z];
DIGIT : [0-9];

EOL: '\n';

IDENT : (LETTER + '$' + '_')(LETTER + DIGIT + '$' + '_')*;

POSITIVE_DIGIT : [1-9];
INT : '0' POSITIVE_DIGIT*;


NUM : DIGIT+;
SIGN : '+' | '-' | /* epsilon */;

OBRACE : '{';
CBRACE : '}';
CPARENT : ')';
OPARENT : '(';

SEMI : ';';

PRINTLN : 'println';
PRINT : 'print';

SPACES : ('\n'|' ') {skip(); };



// Deca lexer rules.
DUMMY_TOKEN: .; // A FAIRE : Règle bidon qui reconnait tous les caractères.
                // A FAIRE : Il faut la supprimer et la remplacer par les vraies règles.
