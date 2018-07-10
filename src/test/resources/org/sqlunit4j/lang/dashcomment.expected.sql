DASH                     <->
DASH                     <->
WORD                     <Saved>
WS                       <>
NEWLINE                  <>
CALL                     <call>
WS                       <>
WORD                     <LOAD1>
LEFT_PAREN               <(>
OPEN_STRING              <'>
STRING_LITERAL           <013536>
CLOSE_STRING             <'>
RIGHT_PAREN              <)>
SEMI_COLON               <;>
NEWLINE                  <>
CALL                     <call>
WS                       <>
WORD                     <LOAD2>
LEFT_PAREN               <(>
OPEN_STRING              <'>
STRING_LITERAL           <013536>
CLOSE_STRING             <'>
RIGHT_PAREN              <)>
SEMI_COLON               <;>
NEWLINE                  <>
===========
(
statements (
statement (
dashComment - - Saved   \r\n
) (
callStatement call (
statementBody   LOAD1 (
 (
stringLiteral ' 013536 '
) 
)
) ; \r\n
)
) (
statement (
callStatement call (
statementBody   LOAD2 (
 (
stringLiteral ' 013536 '
) 
)
) ; \r\n
)
) <EOF>
)