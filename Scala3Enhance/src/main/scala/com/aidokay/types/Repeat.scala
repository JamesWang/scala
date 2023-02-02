package com.aidokay.types

enum Mood:
  case Surprised, Angry, Neutral


val errmsg = "Please enter a word, a positive integer count, and \n" +
  "a mood (one of 'angry', 'surprised', or 'neutral')"


object Mood:
  import scala.util.CommandLineParser.FromString

  /**
   * Tell the compiler how to transform a command line argument string into a Mood
   * Put it here, the compiler will look here when searching for a given FromString[Mood]
   */
  given moodFromString: FromString[Mood] with
    def fromString(s: String): Mood =
      s.trim.toLowerCase match
        case "angry" => Mood.Angry
        case "surprised" => Mood.Surprised
        case "neutral" => Mood.Neutral
        case _ => throw new IllegalArgumentException(errmsg)


@main def repeat(word: String, count: Int, mood: Mood): Unit =
  val msg =
    if count > 0 then
      val words = List.fill(count)(word.trim)
      val punc = mood match
        case Mood.Angry => "!"
        case Mood.Neutral => ""
        case Mood.Surprised => "?"
      val sep = punc + " "

      words.mkString(sep) + punc

    else errmsg

  println(msg)