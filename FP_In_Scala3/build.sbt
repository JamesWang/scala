ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.3"

lazy val root = (project in file("."))
  .settings(
    name := "FP_In_Scala3"
  )

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.2.13",
  "org.scalatest" %% "scalatest" % "3.2.13" % "test"
)