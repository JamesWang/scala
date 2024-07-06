### Mix with Cake Pattern
* The sort of developer who puts methods on a trait, requiring users to mix it with the
cake pattern, is going straight to hell. It leaks internal implementation detail to public
APIs, bloats bytecode, makes binary compatibility basically impossible, and confuses IDE
auto-completers.

```
A slightly more verbose form of implicit class that avoids the allocation and is therefore
preferred:

    implicit final class DoubleOps(private val x: Double) extends AnyVal {
        def sin: Double = java.lang.Math.sin(x)
    }
```

### Type class coherence
* Typeclasses look superficially similar to algebraic interfaces from the
  previous chapter, but algebras do not have to be coherent
* Typeclass coherence is primarily about consistency, and the consistency gives us the confidence
  to use implicit parameters. It would be difficult to reason about code that performs differently
  depending on the implicit imports that are in scope. Typeclass coherence effectively says that imports
  should not impact the behaviour of the code.
  Additionally, typeclass coherence allows us to globally cache implicits at runtime and save memory
  allocations, gaining performance improvements from reduced pressure on the garbage collector.

### Totality
* A function is total if it is defined for all inputs of its corresponding type, or in other words, 
  if a function returns the output on any possible values of the input types.

### About use **scalaOptions**
* Note: 
  * Just put it in depended project(s) will not work, either directly put into the project int which it 
    is used, or if multiple places use it, put it in a common settings, i.e:
```sbt
  lazy val commonSettings = Seq(
    scalacOptions += "-Ymacro-annotations"
  )

  lazy val roundOne = (project in file("round-one"))
        .dependsOn(common)
        .settings(commonSettings *)
        ...
```

### Cake pattern with mix in details
* The sort of developer who puts methods on a trait, requiring users to mix it with the 
  cake pattern, is going straight to hell. It leaks internal implementation detail to public
  APIs, bloats bytecode, makes binary compatibility basically impossible, and confuses IDE
  autocompleters.
* Use extension methodology will be better, to get better runtime cost, using extends **AnyVal**, i.e:
```scala
  implicit final class DoubleOps(private val x: Double) extends AnyVal {
    def sin: Double = java.lang.Math.sin(x)
  }
```

### Avoid using java.net.URL
* it uses DNS to resolve the hostname part when performing toString, equals or hashCode.
  Apart from being insane, and very very slow, these methods can throw I/O exceptions (are not pure), 
  and can change depending on the network configuration

### Configure kind-projector
```sbt
ThisBuild / scalacOptions += "-P:kind-projector:underscore-placeholders"
        ...

.settings(
  addCompilerPlugin( "org.typelevel" %% "kind-projector" % "0.10.3"),
        ...
)

```

### Free Monad
* The idea behind Free Monad is that all of our **computation** become a value. 
  The idea is that whenever we define our application, it does not really execute itself,
  but constructs an abstract syntax tree that describes the application that we can run later.
* With Free Monad, we can build the computation blueprint, and run it on different interpreter
* Doobie represents its SQL queries with free objects, which means that they are just data structure
  that specify the computation to be performed against a database.
  It's just a description of the computation and not the computation itself, we can either run it or
  do anything else with it.

### setup kind-projector in build.sbt
Note: for multi-project builds - put addCompilerPlugin clause into settings section for each sub-project.
```
.settings(
    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.3" cross CrossVersion.full),
    libraryDependencies ++= Seq(
      "org.scalaz" %% "scalaz-core" % "7.3.8",
      "org.scalaz" %% "scalaz-effect" % "7.3.8"
    ) ++ testLibs
  )

```

### Functor
* map on Functor is a way of sequencing computations on values
* Future
  Future is a functor that sequences asynchronous computation by queueing them and applying them as their 
  predecessors complete
* Single argument functions are functors
* Function composition is sequencing (map)

### Contravariant Functor
* contramap only makes sense for data types that represent transformations
* 