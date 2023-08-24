name := "rtm-root"

inThisBuild(
  Seq(
    organization := "com.github.tmtsoftware.rtm",
    scalaVersion := "3.3.0",
    version      := "0.3.2",
    resolvers += "jitpack" at "https://jitpack.io",
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-feature",
      "-unchecked",
      "-deprecation"
    )
  )
)

lazy val rtm = project.settings(
  Settings.addAliases(),
  testOptions += Tests.Argument(TestFrameworks.JUnit, "-v"),
  libraryDependencies += "org.scalatest" %% "scalatest"         % "3.2.16",
  libraryDependencies += "net.aichler"    % "jupiter-interface" % "0.11.1" % Provided,
  libraryDependencies += "com.lihaoyi"   %% "scalatags"         % "0.12.0",
  libraryDependencies += "com.github.sbt" % "junit-interface"   % "0.13.3"
)
