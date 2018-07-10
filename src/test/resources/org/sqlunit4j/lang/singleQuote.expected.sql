SELECT                   <select>
WS                       <>
SYMBOL                   <*>
WS                       <>
WORD                     <from>
WS                       <>
WORD                     <table>
WS                       <>
WORD                     <X>
WS                       <>
EQUALS                   <=>
WS                       <>
OPEN_STRING              <'>
STRING_LITERAL           <${X}>
CLOSE_STRING             <'>
SEMI_COLON               <;>
NEWLINE                  <>
===========
(
statements (
statement (
selectStatement select (
statementBody   *   from   table   X   =   (
stringLiteral ' ${X} '
)
) ; \r\n
)
) <EOF>
)