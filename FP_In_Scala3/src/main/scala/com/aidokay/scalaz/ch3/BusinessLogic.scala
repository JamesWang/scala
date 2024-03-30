package com.aidokay.scalaz.ch3

import com.aidokay.scalaz.ch3.Algebras.{Drone, Epoch, MachineNode, Machines}
import scalaz.Scalaz.{ToBindOps, ToFunctorOps}
import scalaz.syntax.all.ApplicativeIdV
import scalaz.syntax.foldable.ToFoldableOps
import scalaz.{Monad, NonEmptyList}

import scala.concurrent.duration.DurationInt

object BusinessLogic {
  final case class WorldView(
                              backlog: Int,
                              agents: Int,
                              managed: NonEmptyList[MachineNode],
                              alive: Map[MachineNode, Epoch],
                              pending: Map[MachineNode, Epoch],
                              time: Epoch
                            )

  trait DynAgents[F[_]] {
    def initial: F[WorldView]

    def update(old: WorldView): F[WorldView]

    def act(world: WorldView): F[WorldView]
  }

  final class DynAgentsModule[F[_] : Monad](D: Drone[F], M: Machines[F]) extends DynAgents[F] {
    override def initial: F[WorldView] = for {
      db <- D.getBacklog
      da <- D.getAgents
      mm <- M.getManaged
      ma <- M.getAlive
      mt <- M.getTime
    } yield WorldView(db, da, mm, ma, Map.empty, mt)

    def update(old: WorldView): F[WorldView] = for {
      snap <- initial
      changed = symdiff(old.alive.keySet, snap.alive.keySet)
      pending = (old.pending -- changed).filterNot {
        case (_, started) => (snap.time - started) >= 10.minutes
      }
      updated = snap.copy(pending = pending)
    } yield updated

    private def symdiff[T](a: Set[T], b: Set[T]): Set[T] = (a union b) -- (a intersect b)

    private object NeedsAgent {
      def unapply(world: WorldView): Option[MachineNode] = world match {
        case WorldView(backlog, 0, managed, alive, pending, _) if backlog > 0 && alive.isEmpty && pending.isEmpty => Option(managed.head)
        case _ => None
      }
    }

    import scalaz.syntax.std.list.ToListOpsFromList
    private object Stale {
      def unapply(world: WorldView): Option[NonEmptyList[MachineNode]] = world match {
        case WorldView(backlog, _, _, alive, pending, time) if alive.nonEmpty =>
          (alive -- pending.keys).collect {
            case (n, started) if backlog == 0 && (time - started).toMinutes % 60 >= 58 => n
            case (n, started) if (time - started) >= 5.hours => n
          }.toList.toNel.toOption
        case _ => None
      }
    }

    override def act(world: WorldView): F[WorldView] = world match {
      case NeedsAgent(node) =>
        for {
          _ <- M.start(node)
          update = world.copy(pending = Map(node -> world.time))
        } yield update
      case Stale(nodes) =>
        nodes.foldLeftM(world) {
          (world, n) => {
            for {
              _ <- M.stop(n)
              update = world.copy(pending = world.pending + (n -> world.time))
            } yield update
          }
        }
      case _ => world.pure[F]
    }
  }
}
