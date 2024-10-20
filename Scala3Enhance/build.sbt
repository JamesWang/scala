ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "Scala3Enhance"
  )

val zioVersion = "2.1.6"


libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.12.0",
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-streams" % zioVersion,
  "dev.zio" %% "zio-http" % "0.0.5",
  "dev.zio" %% "zio-direct" % "1.0.0-RC7",
)

