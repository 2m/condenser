lazy val condenser =
  crossProject(JVMPlatform, NativePlatform)
    .settings(
      organization := "lt.dvim.condenser",
      name := "condenser",
      description := "Improve Steam efficiency by consolidating game catalogs",
      libraryDependencies ++= Seq(
        "org.scodec" %%% "scodec-core" % "1.10.3+15-82cb912a",
        "com.lihaoyi" %%% "utest" % "0.6.0" % Test
      ),
      resolvers += Resolver.sonatypeRepo("snapshots"), // shapeless native
      testFrameworks += new TestFramework("utest.runner.Framework")
    )
    .nativeSettings(
      nativeLinkStubs := true
    )

lazy val condenserJVM = condenser.jvm
lazy val condenserNative = condenser.native

lazy val root = project
  .in(file("."))
  .aggregate(condenserJVM, condenserNative)

scalafmtOnCompile in ThisBuild := true
scalafmtVersion in ThisBuild := "1.3.0"
