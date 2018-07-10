CALL                     <call>
WS                       <>
WORD                     <XX>
LEFT_PAREN               <(>
OPEN_STRING              <'>
STRING_LITERAL           <A>
CLOSE_STRING             <'>
COMMA                    <,>
OPEN_STRING              <'>
CLOSE_STRING             <'>
RIGHT_PAREN              <)>
SEMI_COLON               <;>
NEWLINE                  <>
===========
(
statements (
statement (
callStatement call (
statementBody   XX (
 (
stringLiteral ' A '
) , (
stringLiteral ' '
) 
)
) ; \r\n
)
) <EOF>
)