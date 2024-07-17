package com.aidokay.akka.tutorial.behavior

import akka.actor.testkit.typed.scaladsl.{LogCapturing, ScalaTestWithActorTestKit}
import org.scalatest.wordspec.AnyWordSpecLike

class StashDocSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike with LogCapturing {

  import stash.DB
  import stash.DataAccess
  import scala.concurrent.Future
  import akka.Done

  "Stashing docs" must {

    "illustrate stash and unstashAll" in {

      val db = new DB {
        override def save(id: String, value: String): Future[Done] = Future.successful(Done)

        override def load(id: String): Future[String] = Future.successful("TheValue")
      }
      val dataAccess = spawn(DataAccess(id = "17", db))
      val getProbe = createTestProbe[String]()
      dataAccess ! DataAccess.Get(getProbe.ref)
      getProbe.expectMessage("TheValue")

      val saveProbe = createTestProbe[Done]()
      dataAccess ! DataAccess.Save("UpdatedValue", saveProbe.ref)
      dataAccess ! DataAccess.Get(getProbe.ref)
      saveProbe.expectMessage(Done)
      getProbe.expectMessage("UpdatedValue")

      dataAccess ! DataAccess.Get(getProbe.ref)
      getProbe.expectMessage("UpdatedValue")
    }
  }
}