ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "FP_In_Scala3"
  )

val AkkaVersion = "2.8.5"
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.12.0",
  "org.typelevel" %% "kittens" % "3.3.0",
  "org.typelevel" %% "cats-effect" % "3.5.0",
  "io.circe" %% "circe-core" % "0.14.5",
  "io.circe" %% "circe-parser" % "0.14.5",
  "io.circe" %% "circe-refined" % "0.14.5",
  "org.json4s" %% "json4s-jackson" % "4.0.6",
  "dev.zio" %% "zio" % "2.0.13",
  "dev.zio" %% "zio-streams" % "2.0.13",
  "io.github.iltotore" %% "iron" % "2.0.0",
  "io.github.iltotore" %% "iron-cats" % "2.0.0",
  "io.github.iltotore" %% "iron-circe" % "2.0.0",
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "org.scalaz" %% "scalaz-core" % "7.3.8",
  "org.scalaz" %% "scalaz-effect" % "7.3.8",
  "ch.qos.logback" % "logback-classic" % "1.5.6",
  "org.scalacheck" %% "scalacheck" % "1.17.1" % Test,
  "org.scalactic" %% "scalactic" % "3.2.18",
  "org.scalatest" %% "scalatest" % "3.2.18" % "test",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
)