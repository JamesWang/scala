package com.aten.scala.akka.counter

import akka.actor.ActorSystem
import akka.actor.Props

object RunCount extends App {

  val system = ActorSystem("word-count-system")
  val m = system.actorOf(Props[WordCountMaster], name="word-count-m")
  m ! StartCounting("src/main/scala/aten/util/", 2 )
}