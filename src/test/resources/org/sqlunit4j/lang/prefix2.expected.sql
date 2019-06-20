WORD                     <INSERT>
WS                       <>
WORD                     <INTO>
WS                       <>
WORD                     <x>
WS                       <>
WORD                     <VALUES>
WS                       <>
NUMBER                   <1>
COMMA                    <,>
NUMBER                   <2>
SEMI_COLON               <;>
NEWLINE                  <>
WORD                     <cl>
COLON                    <:>
CALL                     <CALL>
WS                       <>
WORD                     <qcmd>
LEFT_PAREN               <(>
OPEN_STRING              <'>
CLOSE_STRING             <'>
COMMA                    <,>
OPEN_STRING              <'>
CLOSE_STRING             <'>
COMMA                    <,>
NUMBER                   <12>
RIGHT_PAREN              <)>
SEMI_COLON               <;>
NEWLINE                  <>
SELECT                   <SELECT>
WS                       <>
SYMBOL                   <*>
WS                       <>
WORD                     <FROM>
WS                       <>
WORD                     <foo_table>
SEMI_COLON               <;>
NEWLINE                  <>
===========
(
statements (
statement (
otherStatement (
statementBody INSERT   INTO   x   VALUES   1 , 2
) ; \r\n
)
) (
statement (
prefixPhrase cl : (
prefixBody (
reservedWord CALL
)   qcmd (
 (
stringLiteral ' '
) , (
stringLiteral ' '
) , 12 
)
) ; \r\n
)
) (
statement (
selectStatement SELECT (
statementBody   *   FROM   foo_table
) ; \r\n
)
) <EOF>
)