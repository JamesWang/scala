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
=======
### Intersection Types and Union Types
#### Intersection types
    An intersection type is a sub-type of all combination types of its constituent types
    The order of appearance of types in an intersection type does not matter

#### Union types
    A union type indicate that an object is an instance of at least one mentioned type
    A union type is super-type of all combination of its constituent types
    Union type is the nearest common super-type - least upper bound ->LUB


Scala's type system forms a mathematical lattice, A lattice is a partial order 
in which any two types have both a unique LUB and a unique greatest lower bound

Intersection Types => the Greatest Lower Bound => GLB
Union Types        => the Least Upper Bound    => LUB

#### Transparent trait
    Indicate a trait you don't want the name of the trait to appear in inferred types
**The transparent modifier only affects type inference. You can still use transparent traits 
as types if you write them out explicitly**

**Java stores the element type of the array at runtime, so every time an array element is update,
the enw element value is checked against the stored type.**

**JVM’s underlying run-time model treats arrays as covariant**

### Algebraic data types - ADT
**An algebraic data type (ADT) is a data type composed of a finite set of cases.**
**ADTs are natural way to express domain models. each case represents one data constructor.**

    In Scala, a sealed family of case classes forms an ADT so long as at least one of 
    the cases takes parameters.

    Enumerated data types -> EDT 
        - is a sealed family of case classes in which none of the cases take paramters
