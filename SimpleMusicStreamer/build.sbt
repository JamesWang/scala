ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.0"

ThisBuild / scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _)) => Seq("-Ykind-projector:underscores")
    case Some((2, 12 | 13)) => Seq("-Xsource:3", "-P:kind-projector:underscore-placeholders")
  }
}

lazy val root = (project in file("."))
  .settings(
    name := "SimpleMusicStreamer"
  )

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.0.4",
  "dev.zio" %% "zio-streams" % "2.0.4",
  "dev.zio" %% "zio-http" % "0.0.3",
  "org.apache.tika" % "tika-core" % "2.6.0",
//  "org.apache.tika" % "tika-parsers" % "2.6.0" pomOnly(),
  "org.apache.tika" % "tika-parsers-standard-package" % "2.6.0",
  "ch.qos.logback" % "logback-classic" % "1.4.5" % Test,
  "org.scalatest" %% "scalatest" % "3.2.14" % Test
)