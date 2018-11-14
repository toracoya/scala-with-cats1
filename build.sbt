name := "scala-with-cats"
organization := "com.toracoya"

scalaVersion in ThisBuild := "2.12.7"
scalacOptions in ThisBuild ++= Seq(
  "-language:_",
  "-Ypartial-unification",
  "-Xfatal-warnings",
  "-deprecation"
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.4.0"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7")
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
