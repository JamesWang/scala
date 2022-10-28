package com.aidokay.music

import akka.actor.{Actor, ActorLogging, Props, SupervisorStrategy}
import akka.io.IO
import akka.io.Tcp._

import java.net.InetSocketAddress

class MusicManager(handlerClass: Class[_]) extends Actor with ActorLogging {
  import akka.io.Tcp
  import context.system

  override val supervisorStrategy: SupervisorStrategy =
    SupervisorStrategy.stoppingStrategy

  override def preStart(): Unit = {
    // lookup TCP driver(manager), IO.apply() requires an implicit ActorSystem
    // the returned manager receives I/O command and instantiates worker action
    val manager = IO(Tcp)
    manager ! Bind(self, new InetSocketAddress("0.0.0.0", 10255))

    // flow controll
    // 1. Ack-based : driver notifies the writer when writes have succeeded
    // 2. Nack-based: driver notifies the writer when writes have failed

    // An acknowledged write does not mean acknowledged delivery or storage;
    // receiving an ack for a write signals that the I/O driver has successfully processed the write.
    // The Ack/Nack protocol described here is a means of flow control not error handling.
    // In other words, data may still be lost, even if every write is acknowledged.
  }
  override def receive: Receive = {
    case Bound(localAddress) =>
      log.info(s"Listening on port {}", localAddress.getPort)
    case CommandFailed(Bind(_, local, _, _, _)) =>
      log.warning(s"Cannot bind to [$local]")
      context.stop(self)
    case Connected(remote, _) =>
      log.info(s"Received connection from {}", remote)
      val handler = context.actorOf(Props(handlerClass, sender(), remote))
      sender() ! Register(handler, keepOpenOnPeerClosed = true)
  }
}
