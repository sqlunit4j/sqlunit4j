parser grammar SQLUnit4JParser; 
@header{
	package org.sqlunit4j.lang;
}
options { tokenVocab=SQLUnit4JLexer; }

statements:
statement*
EOF?;
 
statement
  :
  annotationPhrase*
  dashComment*
  indent?
  (
  prefixPhrase
 | printPhrase
 | selectStatement
 | deleteStatement
 | updateStatement
 | callStatement
 | withStatement
 | defStatement
 | otherStatement
  );
  
indent:
IDENT_WS+
; 
annotationPhrase
  : (DASH DASH)? (annotation=AT annotationWord
  (LEFT_PAREN 
  	(annotationParameter)?
  	(COMMA annotationParameter)*
  	RIGHT_PAREN
  )? 
  )+
  WS* NEWLINE
; 
annotationParameter
  :name=WORD EQUALS (number=NUMBER | string=stringLiteral)
  ;
annotationWord
   :WORD|reservedWord;
   
dashComment
  : DASH DASH (~AT ~NEWLINE*)? NEWLINE 
;
prefixPhrase
  : prefix=WORD WS* COLON WS*
    prefixBody SEMI_COLON WS* (NEWLINE|EOF)
;
prefixBody
  :
(WORD|reservedWord|NUMBER|parameter|EQUALS
    |SYMBOL|LEFT_PAREN|RIGHT_PAREN|AT|CLOSE_BRACKET|OPEN_BRACKET|DOLLAR|DASH|stringLiteral|WS|COMMA|COLON
  )* 
  ;

printPhrase
  : PRINT
    printBody WS* SEMI_COLON WS* (NEWLINE|EOF);
    
printBody
  :
(WORD|reservedWord|NUMBER|parameter|EQUALS
    |SYMBOL|LEFT_PAREN|RIGHT_PAREN|AT|CLOSE_BRACKET|OPEN_BRACKET|DOLLAR|DASH|stringLiteral|WS|COLON|COMMA
  )* SEMI_COLON?
  ;
   
selectStatement:
  SELECT statementBody SEMI_COLON WS* (NEWLINE|EOF);

updateStatement:
  UPDATE statementBody SEMI_COLON WS* (NEWLINE|EOF);

deleteStatement:
  DELETE statementBody SEMI_COLON WS* (NEWLINE|EOF);
  
callStatement:
  CALL statementBody SEMI_COLON WS* (NEWLINE|EOF);
   
withStatement:
  WITH statementBody SEMI_COLON WS* (NEWLINE|EOF);

defStatement:
  DEF WS+ variable=WORD WS* EQUALS WS* value=defValue SEMI_COLON (NEWLINE|EOF);
defValue:
  NUMBER | stringLiteral;

otherStatement:
  statementBody SEMI_COLON WS* (NEWLINE|EOF);

statementBody:
  (WORD|reservedWord|NUMBER|parameter|EQUALS
  	|SYMBOL|LEFT_PAREN|RIGHT_PAREN|AT|
  	CLOSE_BRACKET|OPEN_BRACKET|DOLLAR|DASH|stringLiteral|
  	WS|IDENT_WS|NEWLINE|COLON|COMMA
  )+ ;

reservedWord:
	DEF
	|SELECT
	|DELETE
	|UPDATE
	|CALLs
	|WITH;

parameter:
	  DOLLAR OPEN_BRACKET OPEN_BRACKET literal=WORD CLOSE_BRACKET CLOSE_BRACKET
	| DOLLAR OPEN_BRACKET name=WORD CLOSE_BRACKET
	| QUESTION
;
symbol:
   SYMBOL|LEFT_PAREN|RIGHT_PAREN|AT|CLOSE_BRACKET|OPEN_BRACKET|DOLLAR|DASH|EQUALS|COLON|COMMA
   ;
   
stringLiteral:
	OPEN_STRING value=STRING_LITERAL? CLOSE_STRING;