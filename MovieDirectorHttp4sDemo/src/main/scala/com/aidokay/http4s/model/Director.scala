package com.aidokay.music.model

case class Movie(id: String, title: String, year: Int, actors: List[String], director: String)

case class Director(firstName: String, lastName: String) {
  override def toString: _root_.java.lang.String = s"$firstName $lastName"
}
