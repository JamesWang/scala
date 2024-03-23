### Algo in Scala

FindMaxDiff from a List with constraint:
- Bigger number should have bigger index in the List

iterate from the second last element of the List, with the last one as the initial diff, keeps track
of the max number, whenever find a number before it is bigger, then make it as the max, and no other
changes, keep iteration, if the number is not bigger thant max, then calculate the new diff using max
and the number, compare the new diff with prev-diff, keep the bigger one, and keep iteration with the
remaining numbers in the List

### Setup Bloop

- Follow steps here:
  https://scalacenter.github.io/bloop/setup#sbt
  - add this line(version could be diff) to ~/project/plugins.sbt ( if not exist, create plugins.sbt) 

            addSbtPlugin("ch.epfl.scala" % "sbt-bloop" % "1.5.4")

    - run
      
            sbt bloopInstall
            
            ths command will create two files:
              -  .bloop/root.json
              -  .bloop/root-test.json

    - run 
  
            bloop projects

            it will show root and root-test
  
After this setup,compile will be much faster

###Refinement types
 - Nominal subtype
      
        A class (A) inherits from another (B), the first class (A) is said to be a nominal
        subtype of the other one (B). 
        It's a nominal subtype because each type has a name, and the names are explicitly
        declared to have a subtyping relationship.
- Givens
        
        Alias givens - the name to the left of the = sign is an alias for the value to the right
  - import
        
        import ABC.*  -- imports all members except given members
        to import givens, expliictly or 
           - import ABC.given, or
           - import ABC.{given XXX }  --- by type
           - import ABC.{given XXX[?]} -- all sub-type of XXX defined in ABC

  - Context parameters 
  
         Those that are defined in a using clause, compiler will insert context parameters to fix
         any errors due to missing parameter lists.
  - -Xprint:typer
         
         use this option to the compiler, it will show you what your code looks like after all 
         givens have been added by the type checker:
        
     ![img_1.png](img_1.png)
### Scala 2's implicit class in Scala 3 is more intuitive by using **extension**
```scala 3
  extension (list: List[Double])
    def +(other: List[Double]): List[Double] =
      (list zip other).map(t => t._1 + t._2)

  extension [A, C[_]](c: C[A]) {
    def flatMap[B](f: A => C[B])(using e: Execution[C]): C[B] = e.chain(c)(f)
    def map[B](f: A => B)(using e: Execution[C]): C[B] =
      e.chain(c)(f andThen e.create)
  }
```