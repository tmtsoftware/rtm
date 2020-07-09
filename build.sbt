import sbt.Keys.{libraryDependencies, resolvers}

name := "tmt-test-reporter"

libraryDependencies += "org.scalatest" %% "scalatest"        % "3.1.0"
libraryDependencies += "net.aichler"   % "jupiter-interface" % "0.8.3" % Provided

val enableFatalWarnings: SettingKey[Boolean] = settingKey[Boolean]("enable fatal warnings")

inThisBuild(
  Seq(
    organization := "com.github.tmtsoftware.rtm",
    scalaVersion := "2.13.1",
    version := "0.1.0-SNAPSHOT",
    resolvers += "jitpack" at "https://jitpack.io",
    resolvers += "bintray" at "https://jcenter.bintray.com",
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
