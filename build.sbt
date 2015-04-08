name := """DoctorWho"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)

libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.8.7"

libraryDependencies += "com.codesnippets4all" % "quick-json" % "1.0.4"

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.3.1"
