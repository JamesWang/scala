package com.aidokay.util

import java.io.File

import scala.util.matching.Regex


object FileUtils {

  def getListOfFiles(dirName: String, pattern: String) : List[String] = {
    val dir = new File(dirName)
    if (dir.exists && dir.isDirectory) {
      val filenamePattern = new Regex(pattern)
      val allFilenames = dir.listFiles.filter(_.isFile).map(_.getName)
      val allMatched = allFilenames.filter(fn⇒ filenamePattern.matches(fn)).toList
      allMatched
    }
    else List.empty[String]
  }
}
