### Initialization Order

            new Rational(expr1, expr2)

* **expr1** and **expr2** are evaluated before class Rational is initialized
  So **expr1** and **expr2** are available for the initialization of class Rational

            trait RationalTrait:
                val numerArg: Int
                val denomArg: Int
                require(denomArg != 0 )

            new RationalTrait:
                val numerArg = expr1
                val denomArg = expr2 


* for this code, implementing val definition in a subclass(anonymous class) 
  with **expr1** and **expr2** are evaluated only _after_ **superclass** has been initialized,
 so when `require(denomArg !=0 )` is run, `denomArg` is still 0"

* Trait **parametric** fields and **Lazy** vals
  trait parametric fields lets you compute values for fields of a trait before the trait itself is nitialized

            trait RationalTrait(val numerArg: Int, val denomArg: Int):
                require(denomArg != 0)
 
### Abstract Type
* Abstract type declaration is a placeholder for something that will be defined concretely 
  in subclasses.
  Different subclasses can provide different realizations of T
* Abstract type usually is used for **Output** type, parametric type X<T> is used for **Input**
### Refinement types
