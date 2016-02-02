package com.aten.scala.ps.par.chp4.future

import scala.collection.convert.decorateAsScala._

import java.io._
import scala.concurrent._
import scala.io.Source
import aten.util.Utility._
import ExecutionContext.Implicits.global

object FuturesClumsyCallback extends App {
  /**
   * This function takes values from a file which defines 
   * the black list, which will be used as patterns to 
   * match other files
   */
  def blacklistFile( name:String) :Future[List[String]] =
    Future {
      val lines = Source.fromFile( name ).getLines
      lines.filter {  x => !x.startsWith("#") && !x.isEmpty()}.toList
    }
  
  def blakclisted( name:String) : Future[List[String]] ={
    blacklistFile(name).map { patterns=>findFiles(patterns) }
  }
  /**
   * Search file system and match the patterns with the files found
   * and return the file names as a list if they meet the patterns
   */
  def findFiles( patterns: List[String]) :List[String] = {
    val root = new File(".")
   /* for {
       f <- iterateFiles( root, null, true ).asScala.toList
       pat <- patterns
       abspat = root.getCanonicalPath + File.separator + pat
       if f.getCanonicalPath.contains( abspat)
    } yield f.getCanonicalPath*/
    List("")
  }
  
  blacklistFile(".gitignore").foreach {
    case lines => 
      val files = findFiles(lines)
      log(s"matches: ${files.mkString("\n")}")
  }
  Thread.sleep(1000)
}