LINE_COMMENT(Hidden)             <//Test>
SELECT                   <select>
WS                       <>
SYMBOL                   <*>
WS                       <>
WORD                     <from>
WS                       <>
WORD                     <table>
SEMI_COLON               <;>
NEWLINE                  <>
===========
(
statements (
statement (
selectStatement select (
statementBody   *   from   table
) ; \r\n
)
) <EOF>
)