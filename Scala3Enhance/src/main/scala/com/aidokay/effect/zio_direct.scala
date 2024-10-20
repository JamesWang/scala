package com.aidokay.effect

import zio.direct.{defer, run}
import zio.{UIO, ULayer, ZIO, ZIOAppDefault, ZLayer}

object zio_direct extends ZIOAppDefault {

  trait Bread:
    val eat: UIO[Unit] = ZIO.debug("Bread: Eating")

  class BreadStoreBought extends Bread


  val live: ULayer[BreadStoreBought] = ZLayer.succeed:
    BreadStoreBought()

  private val purchaseBread =
      defer[BreadStoreBought]:
        ZIO.debug("Buying bread").run
        BreadStoreBought()

  private val storeBoughtBread =
      ZLayer.fromZIO:
        purchaseBread

  private val eatBread: ZIO[Bread, Nothing, Unit] =
      ZIO.serviceWithZIO[Bread]:
        bread => bread.eat


  class Dough:
    val letRise: UIO[Unit] = ZIO.debug("Dough: rising")

  object Dough:
    val fresh =
      ZLayer.fromZIO:
        defer:
          ZIO.debug("Dough: Mixed").run
          Dough()

  trait HeatSource
  class Oven extends HeatSource

  object Oven:
    val heated =
      ZLayer.fromZIO:
        defer:
          ZIO.debug("Owen: Heated").run
          Oven()

  class BreadHomeMade(heat: HeatSource, dough: Dough) extends Bread

  val homeMadeBread =
    ZLayer.fromZIO:
      defer:
        ZIO.debug("BreadHomeMade: Baked").run
        BreadHomeMade(
          ZIO.service[Oven].run,
          ZIO.service[Dough].run
        )

  override def run: ZIO[Any, Any, Unit] =
      eatBread.provide(
        //storeBoughtBread
        homeMadeBread,
        Dough.fresh,
        Oven.heated
      )
}
