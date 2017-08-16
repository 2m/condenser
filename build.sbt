organization := "lt.dvim.condenser"
name := "condenser"
description := "Improve Steam efficiency by consolidating game catalogs"

libraryDependencies ++= Seq(
  "org.scodec" %% "scodec-core" % "1.10.3",
  "org.scalatest" %% "scalatest" % "3.0.3" % Test
)

//fork in test := true
//javaOptions in test += "-Xss1k"

scalafmtOnCompile in ThisBuild := true
scalafmtVersion in ThisBuild := "1.2.0"
