package com.aidokay.mfp.contra

import com.aidokay.mfp.contra.ContraVariant.flip

trait ProFunctor[F[_, _]] {
  //   d     ---->  f a d
  //   ^           /
  //   c   f b c
  //    b   <---- a
  def dimap[A, B, C, D]: (A => B) => (C => D) => F[B, C] => F[A, D] =
    (f: (A => B)) => (g: (C => D)) => lmap(f) compose (rmap[B, C, D](g))

  def lmap[A, B, C]: (A => B) => F[B, C] => F[A, C] = (f: (A => B)) => dimap(f)(identity[C])

  def rmap[A, B, C]: (B => C) => F[A, B] => F[A, C] =
    dimap[A, A, B, C](identity[A])

  def compose[A, B, C]: (A => B) => (C => A) => C => B = f => g => c => f(g(c))
}

object ProFunctor {
  given functionProfunctor: ProFunctor[Function1] = new ProFunctor[Function1] {
    override def dimap[A, B, C, D]: (A => B) => (C => D) => (B => C) => A => D =
      ab => cd => bc => cd compose bc compose ab

    override def lmap[A, B, C]: (A => B) => (B => C) => A => C =
      //ab => bc => bc compose ab //flip(compose)
      flip(compose)

    override def rmap[A, B, C]: (B => C) => (A => B) => A => C =
      //bc => ab => bc compose ab
      compose
  }


  def main(args: Array[String]): Unit = {
    extension (list: List[String]) {
      def dimap: (String => Int) => ((Int, Int) => Int) => (Int => String) => String =
        f => g => h => h(list.map[Int](f).reduce(g))

      def sumAsString: String = dimap(_.toInt)(_+_)(_.toString)
    }

    val stringDigits = List("1", "2", "3", "4", "5", "6")
    println(stringDigits.sumAsString)

  }
}
