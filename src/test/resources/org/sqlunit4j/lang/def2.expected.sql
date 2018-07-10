DEF                      <def>
WS                       <>
WORD                     <foo>
EQUALS                   <=>
NUMBER                   <123>
SEMI_COLON               <;>
NEWLINE                  <>
DEF                      <def>
WS                       <>
WORD                     <bar>
EQUALS                   <=>
NUMBER                   <123>
SEMI_COLON               <;>
===========
(
statements (
statement (
defStatement def   foo = (
defValue 123
) ; \r\n
)
) (
statement (
defStatement def   bar = (
defValue 123
) ; <EOF>
)
) <EOF>
)