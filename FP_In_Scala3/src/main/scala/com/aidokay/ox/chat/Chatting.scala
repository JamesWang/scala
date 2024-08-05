package com.aidokay.ox.chat

import ox.channels.{ActorRef, Channel, ChannelClosed, Default, DefaultResult, Source, selectOrClosed}
import ox.{Ox, fork, releaseAfterScope}
import sttp.capabilities
import sttp.tapir.*
import sttp.tapir.CodecFormat.TextPlain
import sttp.tapir.server.netty.sync.OxStreams

import java.util.UUID
//https://softwaremill.com/websocket-chat-using-structured-concurrency-ox-and-tapir/#chatting
object Chatting {

  case class Message(v: String)

  given Codec[String, Message, TextPlain] = Codec.string.map(Message.apply)(_.v)

  val chatEndpoint: Endpoint[Unit, Unit, Unit, Ox ?=> Source[Message] => Source[Message], OxStreams & capabilities.WebSockets] = endpoint.get
    .in("chat")
    .out(webSocketBody[Message, TextPlain, Message, TextPlain](OxStreams))

  type ChatMemberId = UUID

  case class ChatMember(id: ChatMemberId, channel: Channel[Message])

  object ChatMember {
    def create: ChatMember = ChatMember(
      UUID.randomUUID(),
      Channel.bufferedDefault[Message]
    )
  }

  class ChatRoom {
    private var members: Map[ChatMemberId, ChatMember] = Map()

    def connected(m: ChatMember): Unit =
      members = members + (m.id -> m)
      println(s"Connected: ${m.id}, number of members: ${members.size}")

    def disconnected(c: ChatMember): Unit = {
      members -= c.id
      println(s"Disconnected: ${c.id}, number of members: ${members.size}")
    }

    def incoming(message: Message): Unit = {
      println(s"Broadcasting: ${message.v}")
      members = members.flatMap { (id, member) =>
        selectOrClosed(member.channel.sendClause(message), Default(())) match {
          case member.channel.Sent() => Some((id, member))
          case _: ChannelClosed =>
            println(s"Channel of member $id closed, removing from members")
            None
          case DefaultResult(_) =>
            println(s"Buffer for member $id full, not sending message")
            Some((id, member))
        }
      }
    }
  }

  def chatProcessor(a: ActorRef[ChatRoom]): OxStreams.Pipe[Message, Message] = incoming => {
    val member = ChatMember.create
    a.tell(_.connected(member))
    fork {
      incoming.foreach { msg =>
        a.tell(_.incoming(msg))
      }
    }

    releaseAfterScope {
      member.channel.done()
      a.tell(_.disconnected((member)))
    }
    member.channel
  }
}
