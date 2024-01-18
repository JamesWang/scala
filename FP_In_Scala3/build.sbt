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
  "io.github.iltotore" %% "iron" % "2.0.0",
  "io.github.iltotore" %% "iron-cats" % "2.0.0",
  "io.github.iltotore" %% "iron-circe" % "2.0.0",
  "org.scalactic" %% "scalactic" % "3.2.16",
  "org.scalatest" %% "scalatest" % "3.2.15" % "test"
)