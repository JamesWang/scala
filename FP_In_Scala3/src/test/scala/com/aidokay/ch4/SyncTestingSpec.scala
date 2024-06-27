package com.aidokay.ch4

import akka.actor.testkit.typed.Effect.{NoEffects, Spawned}
import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import com.aidokay.akka.ch4.{SimplifiedManager, SimplifiedWorker}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SyncTestingSpec extends AnyWordSpec with Matchers {
  "Typed actor synchronous testing" must {
    "spawning takes place" in {
      val testKit = BehaviorTestKit(SimplifiedManager())
      testKit.expectEffect(NoEffects)
      testKit.run(SimplifiedManager.CreateChild("adam"))
      testKit.expectEffect(Spawned(SimplifiedWorker(),"adam"))
    }
  }
}
