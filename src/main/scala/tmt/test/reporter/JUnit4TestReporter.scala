package tmt.test.reporter

import org.junit.runner.notification.{Failure, RunListener}
import org.junit.runner.{Description, Result}

class JUnit4TestReporter extends RunListener {
  private var results: Set[StoryResult] = Set.empty
  private val parentPath                = (sys.env ++ sys.props).getOrElse("RTM_PATH", "./target/RTM")
  private val reportFile                = (sys.env ++ sys.props).getOrElse("OUTPUT_FILE", "/testStoryMapping.txt")

  private val fail                                   = Description.createTestDescription("FAILED", "FAILED")
  override def testRunFinished(result: Result): Unit = {
    CommonUtil.generateReport(parentPath, reportFile, results)
    results = Set.empty
  }

  override def testFinished(description: Description): Unit = {
    if (!description.getChildren.contains(fail)) addResult(description.getMethodName, TestStatus.PASSED)
  }

  override def testFailure(failure: Failure): Unit = {
    addResult(failure.getDescription.getMethodName, TestStatus.FAILED)
    failure.getDescription.addChild(fail)
  }

  override def testIgnored(description: Description): Unit = {
    addResult(description.getMethodName, TestStatus.IGNORED)
  }

  private def addResult(name: String, testStatus: String): Unit = {
    val storyResults = CommonUtil.javaTestParser(name, testStatus)
    results ++= storyResults
    println(results)
  }
}
