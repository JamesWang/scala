package com.aidokay.tc.derivation

import com.aidokay.tc.derivation.TypeClassDerive.Show

object Main extends App{
  enum BinaryTree[+A] derives Show:
    case Node(value: A, left: BinaryTree[A], right: BinaryTree[A])
    case Leaf

  given Show[String] with
    override def show(a: String): String = s"|>>$a<<|"

  given Show[Int] with
    override def show(a: Int): String = s"##$a##"

  val showTree = summon[Show[BinaryTree[String]]]
  println(
    showTree.show(BinaryTree.Node("Hello", BinaryTree.Leaf, BinaryTree.Leaf))
  )

  case class Person(firstName: String, lastname: String, age: Int) derives Show

  println(
    summon[Show[Person]].show(Person("Hanks", "Tom", 65))
  )

}
