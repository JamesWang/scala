package com.aidokay.akka.tutorial.actor

import akka.Done
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import com.aidokay.akka.tutorial.actor.ReqResp.CookieMonster.Munch

import java.util.concurrent.atomic.AtomicInteger

object ReqResp {

  final case class Request(query: String, replyTo: ActorRef[Response])
  final case class Response(result: String, times: Int)
  val times : AtomicInteger = new AtomicInteger(0)
  def apply(): Behavior[Request] =
    Behaviors.setup[Request] { context =>
      val child = context.spawn(CookieMonster(context.self), "cookieFabric")
      println("cookieFabric setup once")
      child ! Munch
      Behaviors.receiveMessage[Request] {
        case Request(query, replyTo) =>
          if (times.get() < 5) {
            replyTo ! Response(s"Here are the cookie for [$query]", times.incrementAndGet())
            Behaviors.same
          } else {
            replyTo ! Response("Done", times.get())
            Behaviors.stopped
          }
      }
  }

  object CookieMonster {
    sealed trait Command
    case object Munch extends Command
    import ReqResp.*
    def apply(cookieFabric: ActorRef[Request]): Behavior[Command] =
      Behaviors.setup[Command | Response] { context =>
        Behaviors.receiveMessage {
          case Munch =>
            cookieFabric ! Request("Give me cookie", context.self)
            Behaviors.same
          case Response(resp, times) =>
            context.log.info("nonomnom got cookies: {}", resp)
            if ("Done" != resp) {
              cookieFabric ! Request("Give me more cookies", context.self)
              Behaviors.same
            } else {
              Behaviors.stopped
            }

        }
      }.narrow
  }

  import akka.actor.typed.scaladsl.AskPattern.*
  def main(args: Array[String]): Unit = {
    val system = ActorSystem(ReqResp(), "request-response")

    //system.terminate()
  }
}
