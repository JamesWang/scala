package com.aidokay.algegraic

object SemigroupEx {
  trait Semigroup[F]:
    def op(a: F, b: F): F
  
}
