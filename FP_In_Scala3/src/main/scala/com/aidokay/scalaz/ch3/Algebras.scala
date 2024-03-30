package com.aidokay.scalaz.ch3

import scalaz.NonEmptyList

import scala.annotation.targetName
import scala.concurrent.duration.*

object Algebras {
  final case class Epoch(millis: Long) extends AnyVal {
    @targetName("plus")
    def +(d: FiniteDuration): Epoch = Epoch(millis + d.toMillis)

    @targetName("minus")
    def -(e: Epoch): FiniteDuration = (millis - e.millis).millis
  }

  trait Drone[F[_]]:
    def getBacklog: F[Int]

    def getAgents: F[Int]

  final case class MachineNode(id: String)

  trait Machines[F[_]]:
    def getTime: F[Epoch]

    def getManaged: F[NonEmptyList[MachineNode]]

    def getAlive: F[Map[MachineNode, Epoch]]

    def start(node: MachineNode): F[MachineNode]

    def stop(node: MachineNode): F[MachineNode]
}
