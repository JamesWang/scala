package com.aidokay.tc

import com.aidokay.tc.JsonTC.JsonSerializer

object PersonalInfo {

  case class Address(
    street: String,
    city: String,
    state: String,
    zip: Int
  )

  case class Phone (
    countryCode: Int,
    phoneNumber: Long
  )

  case class Contact (
    name: String,
    addresses: List[Address],
    phones: List[Phone]
  )

  case class AddressBook(contacts: List[Contact])

  object Address:
    given JsonSerializer[Address] with
      override def serialize(t: Address): String =
        import com.aidokay.tc.JsonTC.ToJsonMethods.toJson as asJson
        s"""
           |{
           |  "street": ${t.street.asJson},
           |  "city": ${t.city.asJson},
           |  "state": ${t.state.asJson},
           |  "zip": ${t.zip.asJson}
           |}
           |""".stripMargin

  object Phone:
    given JsonSerializer[Phone] with
      override def serialize(t: Phone): String =
        import com.aidokay.tc.JsonTC.ToJsonMethods.toJson as asJson
        s"""
           |{
           |  "countryCode": ${t.countryCode.asJson},
           |  "phoneNumber": ${t.phoneNumber.asJson}
           |}""".stripMargin

  def main(args: Array[String]): Unit = {
    import com.aidokay.tc.JsonTC.ToJsonMethods.*
    println(List(1,2,3).toJson)
  }
}
