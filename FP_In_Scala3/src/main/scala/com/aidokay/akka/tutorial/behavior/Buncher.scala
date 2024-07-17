package com.aidokay.akka.tutorial.behavior

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

import scala.collection.immutable


object Buncher {
  //Finite State Machine
  sealed trait Event

  final case class SetTarget(ref: ActorRef[Batch]) extends Event

  final case class Queue(obj: Any) extends Event

  case object Flush extends Event

  private case object Timeout extends Event


  sealed trait Data

  case object Uninitialized extends Data

  final case class Todo(target: ActorRef[Batch], queue: immutable.Seq[Any]) extends Data

  final case class Batch(obj: immutable.Seq[Any])

  def apply(): Behavior[Event] = idle(Uninitialized)

  def idle(data: Data): Behavior[Event] = Behaviors.receiveMessage[Event] { message =>
    (message, data) match
      case (SetTarget(ref), Uninitialized) =>
        idle(Todo(ref, Vector.empty))
      case (Queue(obj), t@Todo(_, vec)) =>
        active(t.copy(queue = vec :+ obj))
      case _ => Behaviors.unhandled
  }

  import scala.concurrent.duration._

  def active(todo: Buncher.Todo): Behavior[Event] = Behaviors.withTimers[Event] { timers =>
    timers.startSingleTimer(Timeout, 1.second)
    Behaviors.receiveMessagePartial {
      case Flush | Timeout =>
        todo.target ! Batch(todo.queue)
        idle(todo.copy(queue = Vector.empty))
      case Queue(obj) =>
        active(todo.copy(queue = todo.queue :+ obj))
    }
  }
  
}
