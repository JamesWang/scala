import cats.Functor
import cats.Bifunctor

implicit val functorForOptions: Functor[Option] = new Functor[Option] {
  def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa match
    case None => None
    case Some(a) => Some(f(a))
}

val listOption = List(Some(1), None, Some(2))

Functor[List].compose[Option].map(listOption)(_ + 2).foreach(println)
def needsFunctor[F[_] : Functor, A](fa: F[A]): F[Unit] = Functor[F].map(fa)(_ => ())

def foo: List[Option[Unit]] = {
  val listOptionFunctor = Functor[List].compose[Option]
  type ListOption[A] = List[Option[A]]
  needsFunctor[ListOption, Int](listOption)(listOptionFunctor)
}

foo

trait Semigroup[A]{
  def combine(x:A, y: A) : A
}

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {
  def apply[A](using monoid: Monoid[A]) = monoid
}

given combineAndMonoid : Monoid[Boolean] = new Monoid[Boolean] {
  override def combine(x: Boolean, y: Boolean): Boolean = x && y
  // && : a combines with empty should depend on a's value
  override def empty: Boolean = true
}

given combineOrMonoid: Monoid[Boolean] = new Monoid[Boolean]:

  override def empty = false

  override def combine(x: Boolean, y: Boolean) = x || y

given combineXnorMonoid: Monoid[Boolean] = new Monoid[Boolean]:
  override def empty = true

  override def combine(x: Boolean, y: Boolean) = (!x || y) && (x || !y)

given combineEitherMonoid: Monoid[Boolean] = new Monoid[Boolean]:
  override def empty = true

  override def combine(x: Boolean, y: Boolean) = (x && !y) || (!x && y)

cats.Semigroup[String].combine("Hi ", "there")





