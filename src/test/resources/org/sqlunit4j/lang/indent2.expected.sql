SELECT                   <select>
WS                       <>
SYMBOL                   <*>
WS                       <>
NEWLINE                  <>
IDENT_WS                 <>
WORD                     <from>
WS                       <>
WORD                     <table>
SEMI_COLON               <;>
NEWLINE                  <>
IDENT_WS                 <>
SELECT                   <select>
WS                       <>
WORD                     <A>
NEWLINE                  <>
IDENT_WS                 <>
COMMA                    <,>
WORD                     <B>
NEWLINE                  <>
IDENT_WS                 <>
COMMA                    <,>
WORD                     <C>
NEWLINE                  <>
IDENT_WS                 <>
WORD                     <from>
WS                       <>
WORD                     <table>
WS                       <>
NUMBER                   <2>
SEMI_COLON               <;>
NEWLINE                  <>
SELECT                   <select>
WS                       <>
SYMBOL                   <*>
WS                       <>
NEWLINE                  <>
IDENT_WS                 <>
WORD                     <from>
WS                       <>
WORD                     <table2>
SEMI_COLON               <;>
===========
(
statements (
statement (
selectStatement select (
statementBody   *   \r\n      from   table
) ; \r\n
)
) (
statement (
indent   
) (
selectStatement select (
statementBody   A \r\n       , B \r\n       , C \r\n     from   table   2
) ; \r\n
)
) (
statement (
selectStatement select (
statementBody   *   \r\n     from   table2
) ; <EOF>
)
) <EOF>
)