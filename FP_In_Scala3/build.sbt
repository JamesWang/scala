ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.4.2"

lazy val root = (project in file("."))
  .settings(
    name := "FP_In_Scala3"
  )

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)

val AkkaVersion = "2.8.6"
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.12.0",
  "org.typelevel" %% "kittens" % "3.3.0",
  "org.typelevel" %% "cats-effect" % "3.5.0",
  "io.circe" %% "circe-core" % "0.14.7",
  "io.circe" %% "circe-parser" % "0.14.9",
  "io.circe" %% "circe-refined" % "0.14.7",
  "org.json4s" %% "json4s-jackson" % "4.0.7",
  "dev.zio" %% "zio" % "2.1.6",
  "dev.zio" %% "zio-streams" % "2.1.6",
  "io.github.iltotore" %% "iron" % "2.5.0",
  "io.github.iltotore" %% "iron-cats" % "2.5.0",
  "io.github.iltotore" %% "iron-circe" % "2.6.0",
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-remote" % AkkaVersion,
  "org.scalaz" %% "scalaz-core" % "7.3.8",
  "org.scalaz" %% "scalaz-effect" % "7.3.8",
  "com.softwaremill.ox" %% "core" % "0.3.1",
  "com.softwaremill.sttp.tapir" %% "tapir-netty-server-sync" % "1.10.13",
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
  "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
  "ch.qos.logback" % "logback-classic" % "1.5.6",
  "org.scalacheck" %% "scalacheck" % "1.17.1" % Test,
  "org.scalactic" %% "scalactic" % "3.2.18",
  "org.scalatest" %% "scalatest" % "3.2.18" % "test",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
)