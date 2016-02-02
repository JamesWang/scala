name := "ScalaExercise"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++=Seq(
	"org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
	"org.apache.commons" % "commons-dbcp2" % "2.1.1"
)