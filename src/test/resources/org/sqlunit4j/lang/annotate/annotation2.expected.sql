DASH                     <->
DASH                     <->
AT                       <@>
WORD                     <Silent>
NEWLINE                  <>
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
annotationPhrase - - @ (
annotationWord Silent
) \r\n
) (
selectStatement select (
statementBody   *   from   table
) ; \r\n
)
) <EOF>
)