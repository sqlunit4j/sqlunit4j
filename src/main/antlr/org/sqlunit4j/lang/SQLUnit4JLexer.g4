lexer grammar SQLUnit4JLexer;

WS  :   (' ' | '\t' | '\f' )+;//
NEWLINE : ('\n' | '\r' )+ -> pushMode(Indent1);
  
LINE_COMMENT :
            '//'
            ( ~('\n'|'\r') )*
            ( '\n'|'\r'('\n')? )?
      -> channel(HIDDEN) ; 

ML_COMMENT
    :   '/*' (.)*? '*/' -> channel(HIDDEN)
    ;

DEF
    : [dD][eE][fF]; 
SELECT
    : [sS][eE][lL][eE][cC][tT]; 
UPDATE
    : [uU][pP][dD][aA][tT][eE];
DELETE
    : [dD][eE][lL][eE][tT][eE];
CALL
    : [cC][aA][lL][lL];
WITH
    : [wW][iI][tT][hH];
PRINT
	: [pP][rR][iI][nN][tT]; 
AT
   : '@';
    
	
DOLLAR:
	'$';
DASH:
	'-';
OPEN_BRACKET:
	'{';
CLOSE_BRACKET:
	'}';
SEMI_COLON:
	';';
EQUALS:
	'=';
COLON:
	':';
COMMA:
	',';
QUESTION:
	'?';
OPEN_STRING
	: '\''
	-> pushMode(InSingleQuotes)
;
WORD 
	:	LETTER (LETTER|DIGIT)*;
NUMBER
	:	DIGIT+([.]DIGIT*)?;
	
LEFT_PAREN
 	: '(';
RIGHT_PAREN
 	: ')';

SYMBOL:~['()@${};:?a-zA-Z0-9\- \t\f\r\n]+;

fragment LETTER	
	: '\u0024'
	| '\u0041'..'\u005a'
	| '\u005f'
	| '\u0061'..'\u007a'
	| '\u00c0'..'\u00d6'
	| '\u00d8'..'\u00f6'
	| '\u00f8'..'\u00ff'
	| '\u0100'..'\u1fff'
	| '\u3040'..'\u318f'
	| '\u3300'..'\u337f'
	| '\u3400'..'\u3d2d'
	| '\u4e00'..'\u9fff'
	| '\uf900'..'\ufaff';

fragment DIGIT 	
	: '\u0030'..'\u0039'
	| '\u0660'..'\u0669'
	| '\u06f0'..'\u06f9'
	| '\u0966'..'\u096f'
	| '\u09e6'..'\u09ef'
	| '\u0a66'..'\u0a6f'
	| '\u0ae6'..'\u0aef'
	| '\u0b66'..'\u0b6f'
	| '\u0be7'..'\u0bef'
	| '\u0c66'..'\u0c6f'
	| '\u0ce6'..'\u0cef'
	| '\u0d66'..'\u0d6f'
	| '\u0e50'..'\u0e59'
	| '\u0ed0'..'\u0ed9'
	| '\u1040'..'\u1049';
	
	
mode InSingleQuotes;
CLOSE_STRING
	: '\''
	-> popMode
;	
STRING_LITERAL
	: ((~[']) | [']['] )+ 
;

END_STRING_ABORMAL:
    'X X X X'*-> popMode,channel(HIDDEN);

mode Indent1;
IDENT_WS  :   (' ' | '\t' | '\f' )+ ->popMode;

END_INDENT:
    'X X X X'*-> popMode,skip;


