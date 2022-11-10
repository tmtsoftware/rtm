import sbt.Keys.{libraryDependencies, resolvers}

name := "tmt-test-reporter"

libraryDependencies += "org.scalatest" %% "scalatest"         % "3.2.14"
libraryDependencies += "net.aichler"    % "jupiter-interface" % "0.11.1" % Provided
libraryDependencies += "com.lihaoyi"   %% "scalatags"         % "0.11.1"
libraryDependencies += "com.github.sbt" % "junit-interface"   % "0.13.3"

val enableFatalWarnings: SettingKey[Boolean] = settingKey[Boolean]("enable fatal warnings")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")

inThisBuild(
  Seq(
    organization := "com.github.tmtsoftware.rtm",
    scalaVersion := "3.2.1",
    version      := "0.3.0",
    resolvers += "jitpack" at "https://jitpack.io",
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-feature",
      "-unchecked",
      "-deprecation",
    )
  )
)

Settings.addAliases()
