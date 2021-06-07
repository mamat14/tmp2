@skip whitespace {
	expression ::= product ('+' product)*;
    product ::= value ('*' value)*;
    value ::= number | variable | '(' expression ')';
    number ::= decimal | integer;
}

decimal ::= ([1-9][0-9]*|'0')?'.'[0-9]+;
integer ::= [1-9][0-9]* |'0';

variable ::= [A-Za-z]+;
whitespace ::= [\s]+;
