package tmt.test.reporter

import org.junit.jupiter.api.extension.{AfterAllCallback, AfterTestExecutionCallback, ExtensionContext}

class KtTestReporter extends AfterTestExecutionCallback with AfterAllCallback {
  var results: List[StoryResult] = List.empty
  override def afterTestExecution(context: ExtensionContext): Unit = {
    val testFailed = context.getExecutionException.isPresent
    addResult(context.getDisplayName, if (testFailed) "FAILED" else "PASSED")
  }

  override def afterAll(context: ExtensionContext): Unit = {
    // fixme :: find a proper way to initialise parentPath
    if (results.nonEmpty) {
      val parentPath = (sys.env ++ sys.props).getOrElse("RTM_PATH", "../../target/RTM")
      val reportFile = "/testStoryMapping.txt"
      CommonUtil.generateReport(parentPath, reportFile, results)
      results = List.empty
    }
  }

  private def addResult(name: String, testStatus: String): Unit = {
    val (testName, stories) = CommonUtil.getTestData(name, Separators.PIPE.toString)

    results ++= stories
      .drop(1)                 // Drop the "|"
      .split(Separators.COMMA) // multiple stories
      .map(x =>
        StoryResult(
          x.split(Separators.PARENTHESIS) { 0 }.strip(),
          testName.strip(),
          testStatus
        )
      )
  }

}
