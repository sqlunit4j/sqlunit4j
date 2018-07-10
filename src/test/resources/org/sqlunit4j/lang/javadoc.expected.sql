ML_COMMENT(Hidden)               </**
  This is a test
*/>
NEWLINE                  <>
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
ML_COMMENT(Hidden)               </* End */>
===========
(
statements (
statement (
otherStatement (
statementBody \r\n (
reservedWord select
)   *   from   table
) ; \r\n
)
) <EOF>
)