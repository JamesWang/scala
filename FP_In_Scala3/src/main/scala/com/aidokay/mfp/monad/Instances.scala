package com.aidokay.mfp.monad

import com.aidokay.mfp.monad.MyMonad.Monad
import com.aidokay.mfp.writer.SimpleWriter

object Instances {
  
  given writerMonad: Monad[SimpleWriter] = new Monad[SimpleWriter] {
    override def pure[A](a: A): SimpleWriter[A] = SimpleWriter.pure(a)

    override def map[A, B](fa: SimpleWriter[A])(f: A => B): SimpleWriter[B] = fa.map(f)

    override def flatMap[A, B](fa: SimpleWriter[A])(f: A => SimpleWriter[B]): SimpleWriter[B] = fa.flatMap(f)
  }
}
