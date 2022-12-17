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


### Abstract members
    
    new RationalTrait:
        val numerArg = expr1
        val denomArg = expr2

    the expressions, expr1 and expr2, are evaluated as part of the initialization of 
    the anonymous class, but the anonymous class is initialized after the RationalTrait,
    
    So the values of numerArg and denomArg are not available during the initialization 
    of RationalTrait 
