package tmt.test.reporter

import org.scalatest.Reporter
import org.scalatest.events._

class TestReporter extends Reporter {
  var results: List[StoryResult] = List.empty
  private val parentPath         = (sys.env ++ sys.props).getOrElse("RTM_PATH", "./target/RTM")
  private val reportFile         = "/testStoryMapping.txt"
  override def apply(event: Event): Unit = {
    event match {
      case x: TestSucceeded => addResult(x.testName, "PASSED")
      case x: TestFailed    => addResult(x.testName, "FAILED")
      case x: TestIgnored   => addResult(x.testName, "IGNORED")
      case x: TestPending   => addResult(x.testName, "PENDING")
      case x: TestCanceled  => addResult(x.testName, "CANCELED")
      case _: RunCompleted  => CommonUtil.generateReport(parentPath, reportFile, results)
      case _                =>
      //    case RunStarting(ordinal, testCount, configMap, formatter, location, payload, threadName, timeStamp) =>
    }
  }

  private def scalaTestParser(name: String, testStatus: String): Array[StoryResult] = {
    val (testName, stories) = CommonUtil.getTestData(name, Separators.PIPE.toString)

    stories
      .drop(1)                 // Drop the "|"
      .split(Separators.COMMA) // multiple stories
      .map(x => StoryResult(x.strip(), testName.strip(), testStatus))
  }

  private def javaTestParser(name: String, testStatus: String): Array[StoryResult] = {
    val (testName, stories) = CommonUtil.getTestData(name, Separators.__)

    stories
      .drop(2)                      // Drop the "__"
      .split(Separators.UNDERSCORE) // multiple stories
      .sliding(2, 2)
      .map(x => x.mkString("-"))
      .map(x => StoryResult(x.strip(), testName.strip(), testStatus))
      .toArray
  }

  private def addResult(name: String, testStatus: String): Unit = {
    val storyResults =
      if (name.contains(Separators.PIPE)) scalaTestParser(name, testStatus)
      else javaTestParser(name, testStatus)

    results ++= storyResults
  }

}
