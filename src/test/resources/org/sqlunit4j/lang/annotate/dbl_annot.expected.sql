AT                       <@>
WORD                     <Verify>
NEWLINE                  <>
AT                       <@>
WORD                     <WaitFor>
LEFT_PAREN               <(>
WORD                     <max>
EQUALS                   <=>
NUMBER                   <2500>
RIGHT_PAREN              <)>
NEWLINE                  <>
SELECT                   <select>
WS                       <>
SYMBOL                   <*>
WS                       <>
WORD                     <from>
WS                       <>
WORD                     <table>
SEMI_COLON               <;>
===========
(
statements (
statement (
annotationPhrase @ (
annotationWord Verify
) \n
) (
annotationPhrase @ (
annotationWord WaitFor
) (
 (
annotationParameter max = 2500
) 
) \n
) (
selectStatement select (
statementBody   *   from   table
) ; <EOF>
)
) <EOF>
)