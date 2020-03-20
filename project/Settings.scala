import sbt._

object Settings {
  def addAliases(): Seq[Setting[_]] = {
    addCommandAlias(
      "buildAll",
      ";set every enableFatalWarnings := true; clean; compile; set every enableFatalWarnings := false"
    ) ++
    addCommandAlias(
      "compileAll",
      ";set every enableFatalWarnings := true; compile; set every enableFatalWarnings := false;"
    )
  }
}
