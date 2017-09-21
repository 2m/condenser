organization := "lt.dvim.condenser"
name := "condenser"
description := "Improve Steam efficiency by consolidating game catalogs"

libraryDependencies ++= Seq(
  "org.scodec" %% "scodec-core" % "1.10.3",
  "com.lihaoyi" %% "utest" % "0.5.3" % Test
)

testFrameworks += new TestFramework("utest.runner.Framework")

scalafmtOnCompile in ThisBuild := true
scalafmtVersion in ThisBuild := "1.2.0"
