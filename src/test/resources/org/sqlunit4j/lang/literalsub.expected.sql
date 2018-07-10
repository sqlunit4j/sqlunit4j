SELECT                   <select>
WS                       <>
SYMBOL                   <*>
WS                       <>
WORD                     <from>
WS                       <>
DOLLAR                   <$>
OPEN_BRACKET             <{>
OPEN_BRACKET             <{>
WORD                     <TABLE>
CLOSE_BRACKET            <}>
CLOSE_BRACKET            <}>
WS                       <>
WORD                     <where>
WS                       <>
DOLLAR                   <$>
OPEN_BRACKET             <{>
OPEN_BRACKET             <{>
WORD                     <COL>
CLOSE_BRACKET            <}>
CLOSE_BRACKET            <}>
WS                       <>
EQUALS                   <=>
WS                       <>
QUESTION                 <?>
SEMI_COLON               <;>
===========
(
statements (
statement (
selectStatement select (
statementBody   *   from   (
parameter $ { { TABLE } }
)   where   (
parameter $ { { COL } }
)   =   (
parameter ?
)
) ; <EOF>
)
) <EOF>
)