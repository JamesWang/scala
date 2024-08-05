package com.aidokay.ox.chat

import com.aidokay.ox.chat.Chatting.{ChatRoom, chatEndpoint, chatProcessor}
import ox.channels.Actor
import ox.supervised
import sttp.shared.Identity
import sttp.tapir.server.netty.NettyConfig
import sttp.tapir.server.netty.sync.NettySyncServer

object ChatWsServer {

  def main(args: Array[String]): Unit = {
    supervised {
      val chatActor = Actor.create(new ChatRoom)
      val chatServerEndpoint = chatEndpoint.serverLogicSuccess[Identity](_ => chatProcessor(chatActor))
      println(s"Starting server at:${NettyConfig.default.host}: ${NettyConfig.default.port}")
      NettySyncServer().addEndpoint(chatServerEndpoint).startAndWait()
    }
  }
}
