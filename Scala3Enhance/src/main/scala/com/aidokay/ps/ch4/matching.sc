def seqToString[T](seq: Seq[T]): String = seq match {
  case head +: tail => s"($head +: ${seqToString(tail)})"
  case Nil => "Nil"
}

println(seqToString(Seq(1,2,3)))
seqToString(Seq.empty[Int])

seqToString(Map("one" -> 1, "two" -> 2, "three" -> 3).toSeq)
seqToString(Map.empty[String, Int].toSeq)

val is: List[(String, Int)] = (("one",1) +: (("two",2) +: (("three",3) +: Nil)))
val map = Map(is*)

def matchThree(seq: Seq[Int]) = seq match
  case Seq(h1, h2, rest*) =>
    println(s"head 1 = $h1, head 2 = $h2, the rest = $rest")
  case _ => println(s"Other! $seq")

matchThree(Seq(1,2,3,4))
matchThree(Seq(1,2,3))