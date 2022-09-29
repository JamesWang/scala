ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.3"

lazy val root = (project in file("."))
  .settings(
    name := "Scala3Enhance"
  )

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.8.0"
)

