PRINT                    <print>
WS                       <>
OPEN_STRING              <'>
STRING_LITERAL           <A:${XYZZY}>
CLOSE_STRING             <'>
SEMI_COLON               <;>
===========
(
statements (
statement (
printPhrase print (
printBody   (
stringLiteral ' A:${XYZZY} '
)
) ; <EOF>
)
) <EOF>
)