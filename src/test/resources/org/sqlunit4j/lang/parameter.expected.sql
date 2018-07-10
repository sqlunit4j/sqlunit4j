CALL                     <call>
WS                       <>
WORD                     <proc>
LEFT_PAREN               <(>
QUESTION                 <?>
COMMA                    <,>
DOLLAR                   <$>
OPEN_BRACKET             <{>
WORD                     <XYZZY>
CLOSE_BRACKET            <}>
RIGHT_PAREN              <)>
SEMI_COLON               <;>
===========
(
statements (
statement (
callStatement call (
statementBody   proc (
 (
parameter ?
) , (
parameter $ { XYZZY }
) 
)
) ; <EOF>
)
) <EOF>
)