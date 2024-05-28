name := "rtm-root"

inThisBuild(
  Seq(
    organization := "com.github.tmtsoftware.rtm",
    scalaVersion := "3.3.3",
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
  libraryDependencies += "org.scalatest" %% "scalatest"         % "3.2.18",
  libraryDependencies += "net.aichler"    % "jupiter-interface" % "0.11.1" % Provided,
  libraryDependencies += "com.lihaoyi"   %% "scalatags"         % "0.13.1",
  libraryDependencies += "com.github.sbt" % "junit-interface"   % "0.13.3"
)
