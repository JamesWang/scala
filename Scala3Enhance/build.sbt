ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.4.2"

lazy val root = (project in file("."))
  .settings(
    name := "Scala3Enhance"
  )

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.12.0"
)

