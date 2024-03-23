ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "FP_In_Scala3"
  )

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.9.0",
  "org.typelevel" %% "kittens" % "3.0.0",
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
  "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0",
  "org.scalaz" %% "scalaz-core" % "7.3.8",
  "org.scalaz" %% "scalaz-effect" % "7.3.8",
"org.scalacheck" %% "scalacheck" % "1.17.0" % Test,
  "org.scalactic" %% "scalactic" % "3.2.16",
  "org.scalatest" %% "scalatest" % "3.2.15" % "test"
)