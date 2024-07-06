import sbt.Keys.scalacOptions

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

ThisBuild / scalacOptions += "-P:kind-projector:underscore-placeholders"

lazy val commonSettings = Seq(
  autoCompilerPlugins := true,
  scalacOptions += "-Ymacro-annotations"
)
lazy val root = (project in file("."))
  .settings(
    name := "scalaz-ex"
  )

lazy val testLibs = Seq(
  "org.scalacheck" %% "scalacheck" % "1.18.0" % Test,
  "org.scalactic" %% "scalactic" % "3.2.19",
  "org.scalatest" %% "scalatest" % "3.2.19" % "test"
)

lazy val common = project.settings(
  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.3" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "org.typelevel" %% "simulacrum" % "1.0.1",
    "eu.timepit" %% "refined-scalaz" % "0.11.2",
    "ch.qos.logback" % "logback-classic" % "1.5.6"
  )
)

lazy val roundOne = (project in file("round-one"))
  .dependsOn(common)
  .settings(commonSettings *)
  .settings(
    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.3" cross CrossVersion.full),
    libraryDependencies ++= Seq(
      "org.scalaz" %% "scalaz-core" % "7.3.8",
      "org.scalaz" %% "scalaz-effect" % "7.3.8"
    ) ++ testLibs
  )
