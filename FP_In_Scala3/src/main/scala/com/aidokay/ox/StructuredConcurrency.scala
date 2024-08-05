package com.aidokay.ox

import ox.channels.*
import ox.{fork, releaseAfterScope, supervised}
import sttp.shared.Identity
import sttp.tapir.*
import sttp.tapir.CodecFormat.*
import sttp.tapir.server.netty.sync.{NettySyncServer, OxStreams}

import java.util.UUID

object StructuredConcurrency {
  private type ChatMemberId = UUID

  case class ChatMember(id: ChatMemberId, channel: Channel[Message])

  private object ChatMember:
    def create: ChatMember = ChatMember(UUID.randomUUID(), Channel.bufferedDefault[Message])

   private class ChatRoom:
    private var members: Map[ChatMemberId, ChatMember] = Map()

    def connected(m: ChatMember): Unit =
      members = members + (m.id -> m)
      println(s"Connected: ${m.id}, number of members: ${members.size}")

    def disconnected(m: ChatMember): Unit =
      members = members - m.id
      println(s"Disconnected: ${m.id}, number of members: ${members.size}")

    def incoming(message: Message): Unit =
      println(s"Broadcasting: ${message.v}")
      members = members.flatMap { (id, member) =>
        selectOrClosed(member.channel.sendClause(message), Default(())) match
          case member.channel.Sent() => Some((id, member))
          case _: ChannelClosed =>
            println(s"Channel of member $id closed, removing from members")
            None
          case DefaultResult(_) =>
            println(s"Buffer for member $id full, not sending message")
            Some((id, member))
      }

  //

  case class Message(v: String) // could be more complex, e.g. JSON including nickname + message

  given Codec[String, Message, TextPlain] = Codec.string.map(Message.apply)(_.v)

  private val chatEndpoint =
    endpoint
      .get
      .in("chat")
      .out(webSocketBody[Message, TextPlain, Message, TextPlain](OxStreams))

  private def chatProcessor(a: ActorRef[ChatRoom]): OxStreams.Pipe[Message, Message] =
    incoming => {
      val member = ChatMember.create

      a.tell(_.connected(member))
      println(s"connected: ${member.id}")
      fork {
        incoming.foreach { msg =>
          println(s"received:  $msg")
          a.tell(_.incoming(msg))
        }
      }

      releaseAfterScope {
        member.channel.done()
        a.tell(_.disconnected(member))
      }

      member.channel
    }

  @main def chatWsServer(): Unit =
    supervised {
      val chatActor = Actor.create(new ChatRoom)
      val chatServerEndpoint = chatEndpoint.serverLogicSuccess[Identity](_ => chatProcessor(chatActor))
      NettySyncServer().addEndpoint(chatServerEndpoint).startAndWait()
      //val serverBinding = NettySyncServer().addEndpoint(chatServerEndpoint).start()
      //println(s"Tapir is running on port ${serverBinding.port}")
      //never
    }
}
