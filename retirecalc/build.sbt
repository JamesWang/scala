ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.3"

lazy val root = (project in file("."))
  .settings(
    name := "RetireCalc"
  )

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.8.0",
  "org.typelevel" %% "cats-effect" % "3.3.14",
  "org.scalactic" %% "scalactic" % "3.2.13",
  "co.fs2" %% "fs2-core" % "3.3.0",
  "co.fs2" %% "fs2-io" % "3.3.0",
  "org.yaml" % "snakeyaml" % "1.33",
  "org.scalatest" %% "scalatest" % "3.2.13" % "test"
)