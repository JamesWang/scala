package com.aidokay.akka

import akka.actor.typed.ActorRef

object AkkaTypedExample {
  final case class Groceries(eggs: Int, flour: Int, sugar: Int, chocolate: Int)

  final case class Dough(weight: Int)

  final case class RawCookies(count: Int)

  final case class ReadyCookies(count: Int)

  sealed trait Command

  case class Put(rawCookies: Int, sender: ActorRef[Baker.Command]) extends Command

  case class Extract(sender: ActorRef[Baker.Command]) extends Command


}
