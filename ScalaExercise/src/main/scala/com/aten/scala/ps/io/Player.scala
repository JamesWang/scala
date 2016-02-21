package com.aten.scala.ps.io

case class Player( name: String, score:Int)

object Player {
  def context( p1: Player, p2:Player ) : Unit = 
    if( p1.score > p2.score )
      println(s"${p1.name} is the winner!") //IO code
    else if( p1.score < p2.score ) 
      println(s"${p2.name} is the winner!") //IO code
    else println("It's a draw!")            //IO code
  

  // Decouple IO code
  def winner( p1:Player, p2: Player ) : Option[Player] = 
    if( p1.score > p2.score ) Some(p1)
    else if( p1.score < p2.score ) Some(p2)
    else None
    
  def contest( p1:Player, p2:Player ):Unit = winner(p1,p2) match {
      case Some(Player(name,_)) => println(s"$name is the winner!")
      case None => println("It's a draw!")
  }
  
  //even better
  def winnerMessage( p:Option[Player]) : String = p map {
    case Player(name,_) => s"$name is the winner!"   
  } getOrElse "It's a draw"
  
  def contest2( p1:Player, p2:Player ) : Unit = 
    println(winnerMessage(winner(p1,p2)) )
    
  trait IO{ def run : Unit }
  
  def PrintLine ( msg : String ) : IO = new IO { def run = println(msg ) }
  
  def contest3( p1: Player, p2:Player ) : IO =
    PrintLine( winnerMessage(winner(p1,p2)))
}