package com.aidokay.eda.fp

import com.aidokay.eda.fp.modeling.Address
import io.circe.parser.decode
import io.circe.syntax.*

object Demo {

  @main def run(): Unit =
    val address = Address("Baker", 221, Some("B"))
    val json = address.asJson.spaces2
    println(json)
    assert(decode[Address](json) == Right(address))

}
