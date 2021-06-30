import sbt.Keys.{libraryDependencies, resolvers}

name := "tmt-test-reporter"

libraryDependencies += "org.scalatest"     %% "scalatest"         % "3.2.9"
libraryDependencies += "net.aichler"        % "jupiter-interface" % "0.9.1" % Provided
libraryDependencies += "com.lihaoyi"       %% "scalatags"         % "0.9.4"
libraryDependencies += "org.scalatestplus" %% "junit-4-13"        % "3.2.9.0"

val enableFatalWarnings: SettingKey[Boolean] = settingKey[Boolean]("enable fatal warnings")

inThisBuild(
  Seq(
    organization := "com.github.tmtsoftware.rtm",
    scalaVersion := "2.13.5",
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
