package com.aten.scala.ps.adt.state

object StateMonad {
  import State._
  
  trait State[S, +A] {
    def apply(s: S): (S, A)
    /**
     * State[S,A] => State[S,B] using f: A=>B
     */    
    def map[B]( f : A=>B ) : State[S,B] = state {
      //apply(_) _:S ==> (S,A)
      //apply(_) returns (S,A) matches (s,a), then return (s,f(a)) : State[S,B]
      apply(_) match { case (s,a) => (s,f(a)) }
    }
    
    def flatMap[B](f: A =>State[S,B]):State[S,B] =
      state {
        apply(_) match { case (s,a) => f(a)(s)}
    }
  }
  
  object State {
    /**
     * convert f: S => (S,A) to State[S,A]
     */
    def state[S, A](f: S => (S, A)) : State[S,A] = new State[S, A] {
      def apply(s: S) = f(s)
    }
    def init[S]: State[S, S] = state[S, S](s => (s, s))
    def modify[S](f: S => S) : State[S,Unit] = init[S] flatMap (s => state(_ => (f(s), ())))
  }
}