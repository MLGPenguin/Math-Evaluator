# Math-Evaluator
Evaluates Mathematical expressions from String form.

Supports 
- Addition (+), Subtraction (-),  Multiplication (*), Division (/) and Exponents (^)
- concise (co-efficient style) multiplication ( n(y) == n * y ).
- Certain Binary Operators for Integers ( | & >> << ) -> ( OR, AND, RIGHT_SHIFT, LEFT_SHIFT )

Also supports a range of common prefix functions like `sin(expression)` and so on.

Using it is simple, just call `Evaluator.eval(String)`

Likewise, you can check the validity of a syntax using `
Evaluator.isValidSyntax(String)`
