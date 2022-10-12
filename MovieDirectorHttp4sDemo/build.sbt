ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.3"

lazy val root = (project in file("."))
  .settings(
    name := "MovieDirectorHttp4sDemo"
  )

val Http4sVersion = "1.0.0-M37"
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-ember-server" % Http4sVersion,
  // /"org.http4s" %% "http4s-core" % C,
  // "org.http4s" %% "http4s-client" % Http4sVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,
  "io.circe" %% "circe-generic" % "0.14.3"
)
