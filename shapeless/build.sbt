ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.11.10"


resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

//incOptions := incOptions.value.withNameHashing(false)


lazy val root = (project in file("."))
  .settings(
    name := "shapeless"
  )


libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.9"
)