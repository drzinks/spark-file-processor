package com.drzinks.scala

import java.io.File

class SparkFileProcessorApp {

  def processFile(file: File) : Unit = {
    val source = scala.io.Source.fromFile(file)
    val lines = try source.mkString finally source.close
    println(lines)
  }

}
