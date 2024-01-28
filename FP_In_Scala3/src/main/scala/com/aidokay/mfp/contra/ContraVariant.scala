package com.aidokay.mfp.contra

object ContraVariant {
  type Op[R, A] = A => R

  trait ContraVariant:
    def contramap[A, B, R](f: B => A)(g: Op[R, A]): Op[R, B]

  def flip[A, B, C]: (A => B => C) => B => A => C =
    (f: A => B => C) => (g: B) => (h: A) => f(h)(g)

  def compose[A, B, C](f: A => B)(g: C => A): C => B = c => f(g(c))

  given opContraVariant: ContraVariant = new ContraVariant():
    override def contramap[A, B, R](f: B => A)(g: Op[R, A]): Op[R, B] =
      flip(compose[A, R, B])(f)(g)
}
