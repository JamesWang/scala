import org.scalacheck.Gen

sealed trait Rank

case class SymRank(s: Char) extends Rank {
  override def toString: String = s.toString
}

case class NumRank(n: Int) extends Rank {
  override def toString: String = n.toString
}

case class Card(suit: Char, rank: Rank) {
  override def toString: String = s"$suit $rank"
}

val suits = Gen.oneOf('♡', '♢', '♤', '♧')
val numbers = Gen.choose(2, 10).map(NumRank)
val symbols = Gen.oneOf('A', 'K', 'Q', 'J').map(SymRank)