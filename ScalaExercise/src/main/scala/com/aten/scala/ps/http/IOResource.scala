package com.aten.scala.ps.http

import java.io.File
import scala.io.Source
import Pure._

case class IOResource(name:String ) extends Resource {
  def exists = new File(name).exists
  def contents = Source.fromFile( name ).getLines.toList
  def contentLength = Source.fromFile(name).count(x=>true)
}
object IOResource {
  implicit val ioResourceLocator : ResourceLocator = name => IOResource(name)
}