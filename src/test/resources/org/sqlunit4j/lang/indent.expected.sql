SELECT                   <select>
WS                       <>
SYMBOL                   <*>
WS                       <>
WORD                     <from>
WS                       <>
WORD                     <table>
SEMI_COLON               <;>
NEWLINE                  <>
IDENT_WS                 <>
CALL                     <call>
WS                       <>
WORD                     <proc>
LEFT_PAREN               <(>
QUESTION                 <?>
RIGHT_PAREN              <)>
SEMI_COLON               <;>
NEWLINE                  <>
IDENT_WS                 <>
CALL                     <call>
WS                       <>
WORD                     <proc2>
LEFT_PAREN               <(>
RIGHT_PAREN              <)>
SEMI_COLON               <;>
NEWLINE                  <>
SELECT                   <select>
WS                       <>
SYMBOL                   <*>
WS                       <>
WORD                     <from>
WS                       <>
WORD                     <table2>
SEMI_COLON               <;>
===========
(
statements (
statement (
selectStatement select (
statementBody   *   from   table
) ; \r\n
)
) (
statement (
indent   
) (
callStatement call (
statementBody   proc (
 (
parameter ?
) 
)
) ; \r\n
)
) (
statement (
indent   
) (
callStatement call (
statementBody   proc2 (
 
)
) ; \r\n
)
) (
statement (
selectStatement select (
statementBody   *   from   table2
) ; <EOF>
)
) <EOF>
)