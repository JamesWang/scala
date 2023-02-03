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

  object Contact:
    given JsonSerializer[Contact] with
      override def serialize(t: Contact): String =
        import com.aidokay.tc.JsonTC.ToJsonMethods.toJson as asJson
        s"""
           |{
           |  "name" : ${t.name.asJson},
           |  "addresses": ${t.addresses.asJson},
           |  "phones": ${t.phones.asJson}
           |}""".stripMargin

  object AddressBook:
    given JsonSerializer[AddressBook] with
      override def serialize(t: AddressBook): String =
        import com.aidokay.tc.JsonTC.ToJsonMethods.toJson as asJson
        s"""
           |{
           |"contacts": ${t.contacts.asJson}
           |}""".stripMargin

  def main(args: Array[String]): Unit = {
    import com.aidokay.tc.JsonTC.ToJsonMethods.*
    println(List(1,2,3).toJson)

    val addressBook =
      AddressBook(
        List(
          Contact( "Bob Smith",
            List(
              Address("12345 Main Street", "San Francisco", "CA", 94105),
              Address("500 State Street", "Los Angeles", "CA", 90007)
            ),
            List(Phone(1, 5558881234), Phone(49, 5558413323))
          )
        )
      )
    println(addressBook.toJson)
  }
}
