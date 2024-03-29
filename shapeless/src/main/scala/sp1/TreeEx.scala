package sp1

object TreeEx {
  sealed trait Tree[_]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
  case class Leaf[A](value: A) extends Tree[A]

}
