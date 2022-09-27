ThisBuild / organization := "com.aidokay"
ThisBuild / name := "playground2"
ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.13.5"

lazy val testLib = Seq(
  "org.scalacheck" %% "scalacheck" % "1.17.0" % "test",
  "org.scalatest" %% "scalatest" % "3.3.0-SNAP3" % "test",
  "org.scalatest" %% "scalatest-flatspec" % "3.2.12" % Test
)

lazy val `core` = (project in file("."))
  .aggregate(`common`, `practice`, `payment-invoice`)

lazy val common = project.settings(
  libraryDependencies ++= Seq(
    "org.slf4j" % "slf4j-api" % "2.0.1",
    "org.slf4j" % "slf4j-simple" % "2.0.0",
    "org.scala-lang" % "scala-reflect" % "2.13.8"
  )
)


lazy val practice = (project in file("practice"))
  .dependsOn(common)
  .settings(
    libraryDependencies ++= Seq(

    ) ++ testLib
  )

lazy val `payment-invoice` = (project in file("payment.invoice"))
  .dependsOn(common)
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.pdfbox" % "pdfbox" % "2.0.26",
      "com.typesafe.play" %% "play-json" % "2.10.0-RC2",
      "org.apache.kafka" % "kafka-clients" % "3.2.3",
      "org.mongodb.scala" %% "mongo-scala-driver" % "4.7.1",
      "com.itextpdf" % "itextpdf" % "5.5.13.3",
    ))
