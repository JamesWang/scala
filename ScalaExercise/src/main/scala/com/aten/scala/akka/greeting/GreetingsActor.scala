package com.aten.scala.akka.greeting

object GreetingsActor extends App {

  import akka.actor.Props
  import akka.actor.ActorSystem
  import akka.actor.Actor
  
  case class Name( name:String )
  
  class GreetingsActor extends Actor {
    def receive = {
      case Name(n) => println( s"Hello $n ")
    }
  }
  val system  = ActorSystem("GreetingSystem")
  val actor = system.actorOf(Props[GreetingsActor], name="greetingActor")
  actor ! Name("James")
  
  Thread.sleep(100)
  system.terminate()
  
}