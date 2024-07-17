package com.aidokay.akka.tutorial.behavior

import akka.Done
import akka.actor.typed.scaladsl.{ActorContext, Behaviors, StashBuffer}
import akka.actor.typed.{ActorRef, Behavior}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object stash {

  trait DB:
    def save(id: String, value: String): Future[Done]

    def load(id: String): Future[String]

  object DataAccess:
    sealed trait Command

    final case class Save(value: String, replyTo: ActorRef[Done]) extends Command

    final case class Get(replyTo: ActorRef[String]) extends Command

    final case class InitialState(value: String) extends Command

    case object SaveSuccess extends Command

    final case class DBError(cause: Throwable) extends Command

    def apply(id: String, db: DB): Behavior[Command] =
      Behaviors.withStash(100) { buffer =>
        Behaviors.setup[Command] {
          context => new DataAccess(context, buffer, id, db).start()
        }
      }

  class DataAccess(
    context: ActorContext[DataAccess.Command],
    buffer: StashBuffer[DataAccess.Command],
    id: String,
    db: stash.DB) {

    import DataAccess.*

    private def start(): Behavior[Command] = {
      context.pipeToSelf(db.load(id)) {
        case Success(value) => InitialState(value)
        case Failure(cause) => DBError(cause)
      }
      //after pipe to self, handle the messages:
      //InitialState or DBError
      Behaviors.receiveMessage[Command] {
        case InitialState(value) =>
          buffer.unstashAll(active(value))
        case DBError(cause) =>
          throw cause
        case other =>
          buffer.stash(other)
          Behaviors.same
      }
    }

    private def active(state: String): Behavior[Command] = {
      Behaviors.receiveMessagePartial {
        case Get(replyTo) =>
          println(s"Get state---$state")
          replyTo ! state
          Behaviors.same
        case Save(value, replyTo) =>
          println(s"saving-----")
          context.pipeToSelf(db.save(id, value)) {
            case Success(_) => SaveSuccess
            case Failure(cause) => DBError(cause)
          }
          //handle above piped messages
          saved(value, replyTo)
      }
    }

    private def saved(state: String, replyTo: ActorRef[Done]): Behavior[Command] = {
      Behaviors.receiveMessage {
        case SaveSuccess =>
          replyTo ! Done
          buffer.unstashAll(active(state))
        case DBError(cause) =>
          throw cause
        case other =>
          buffer.stash(other)
          Behaviors.same
      }
    }
  }
}
