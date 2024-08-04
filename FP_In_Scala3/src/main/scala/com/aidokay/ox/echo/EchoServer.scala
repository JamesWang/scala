package com.aidokay.ox.chat

import ox.ExitCode.Success
import ox.channels.Channel
import ox.{ExitCode, Ox, OxApp, fork}
import sttp.tapir.*
import sttp.shared.Identity
import sttp.tapir.CodecFormat.*
import sttp.tapir.server.netty.sync.{NettySyncServer, OxStreams}

object EchoServer extends OxApp {

  private val wsEndpoint = endpoint.get
      .in("echo")
      .out(webSocketBody[String, TextPlain, String, TextPlain](OxStreams))

  //Ox ?=> Channel[String] => Channel[String]
  private val wsProcessor: OxStreams.Pipe[String, String] =
    //requestStream => requestStream.map(msg => s"You said: $msg")
    requestStream =>
      val outgoing = Channel.bufferedDefault[String]
      fork {
        while true do
          val msg = requestStream.receive()
          outgoing.send(s"You said: $msg")
      }
      outgoing

  private val wsServerEndpoint =
    wsEndpoint.serverLogicSuccess[Identity](_ => wsProcessor)

  override def run(args: Vector[String])(using Ox): ExitCode = {
    val server = NettySyncServer()
    server.port(8080) //specify a port you like
    server.addEndpoint(wsServerEndpoint).startAndWait()
    Success
  }
}
