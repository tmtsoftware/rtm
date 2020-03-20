import sbt.Keys.{libraryDependencies, resolvers}

name := "tmt-test-reporter"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0"

val enableFatalWarnings: SettingKey[Boolean]       = settingKey[Boolean]("enable fatal warnings")

inThisBuild(
  Seq(
    organization := "com.github.tmtsoftware.rtm",
    scalaVersion := "2.13.1",
    version := "0.1.0-SNAPSHOT",
    resolvers += "jitpack" at "https://jitpack.io",
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-Xlint:_,-missing-interpolator",
      "-Ywarn-dead-code"
    )
  )
)

Settings.addAliases()
