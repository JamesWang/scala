package com.aten.scala.ps.kestrel

object ShowMe {
  import com.aten.scala.ps.st.SelfTypeAnnotation._
  
  implicit def kestrel[A]( a: A ) = new {
    def show( sideEffect : A => Unit ) : A = {
      sideEffect(a)
      a
    }
  }
}