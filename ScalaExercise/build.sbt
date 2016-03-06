name := "ScalaExercise"

version := "1.0"

scalaVersion := "2.11.7"

resolvers ++= Seq(
		"Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
		"Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  		"Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
 )

libraryDependencies ++=Seq(
	"org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
	"org.apache.commons" % "commons-dbcp2" % "2.1.1",
	"org.scala-lang.modules" % "scala-async_2.11" % "0.9.5",
	"org.scalaz" %% "scalaz-concurrent" % "7.0.6",
	"org.specs2" %% "specs2-core" % "3.7" % "test",
	"com.typesafe.akka" %% "akka-actor" % "2.4.2",
	"org.scalanlp" %% "breeze" % "0.12",
	"org.scalanlp" %% "breeze-natives" % "0.12",
	 "org.scalanlp" %% "breeze-viz" % "0.12"
)