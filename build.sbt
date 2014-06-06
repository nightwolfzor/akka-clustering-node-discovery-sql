name := """akka-clustering"""

version := "0.1"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-contrib" % "2.3.0",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.0",
  "net.fwbrasil" % "activate-core_2.10" % "1.5",
  "com.github.mauricio" %% "mysql-async" % "0.2.12",
  "net.fwbrasil" % "activate-jdbc-async_2.10" % "1.5",
  "org.scalatest" % "scalatest_2.10" % "2.0" % "test")

