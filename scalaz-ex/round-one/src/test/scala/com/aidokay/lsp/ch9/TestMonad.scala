package com.aidokay.lsp.ch9

import org.scalacheck._
import com.aidokay.lsp.ch9.basicm.Monad
import org.scalacheck.Prop._

object TestMonad extends Properties(name = "TestMonad") {
  def id[A, B, M[_]](implicit M: Monad[M],
                     arbFA: Arbitrary[M[A]],
                     arbFB: Arbitrary[M[B]],
                     arbA: Arbitrary[A],
                     cogenA: Cogen[A]): Prop = {
    val leftIdentity = forAll { as: M[A] =>
      M.flatMap(as)(M.unit(_)) == as
    }
    val rightIdentity = forAll { (a: A, f: A => M[B]) =>
      M.flatMap(M.unit(a))(f) == f(a)
    }
    leftIdentity && rightIdentity
  }

  def associativity[A, B, C, M[_]](
                                    implicit M: Monad[M],
                                    arbMA: Arbitrary[M[A]],
                                    arbMB: Arbitrary[M[B]],
                                    arbMC: Arbitrary[M[C]],
                                    arbB: Arbitrary[B],
                                    arbC: Arbitrary[C],
                                    cogenA: Cogen[A],
                                    cogenB: Cogen[B]): Prop = {
    forAll((as: M[A], f: A => M[B], g: B => M[C]) => {
      val leftSide = M.flatMap(M.flatMap(as)(f))(g)
      val rightSide = M.flatMap(as)(a => M.flatMap(f(a))(g))
      leftSide == rightSide
    })
  }

  def monad[A, B, C, M[_]](implicit M: Monad[M],
                           arbMA: Arbitrary[M[A]],
                           arbMB: Arbitrary[M[B]],
                           arbMC: Arbitrary[M[C]],
                           arbA: Arbitrary[A],
                           arbB: Arbitrary[B],
                           arbC: Arbitrary[C],
                           cogenA: Cogen[A],
                           cogenB: Cogen[B]): Prop = {
    id[A, B, M] && associativity[A, B, C, M]
  }

  property("Monad[Option] and Int => String, String => Long") = {
    monad[Int, String, Long, Option]
  }


}
