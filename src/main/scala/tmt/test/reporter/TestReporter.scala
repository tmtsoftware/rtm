package tmt.test.reporter

import java.io.{File, FileWriter}
import java.nio.file.Files

import org.scalatest.Reporter
import org.scalatest.events._

class TestReporter extends Reporter {
  var results: List[StoryResult] = List.empty

  override def apply(event: Event): Unit = {
    event match {
      case x: TestSucceeded => addResult(x.testName, "PASSED")
      case x: TestFailed    => addResult(x.testName, "FAILED")
      case x: TestIgnored   => addResult(x.testName, "IGNORED")
      case x: TestPending   => addResult(x.testName, "PENDING")
      case x: TestCanceled  => addResult(x.testName, "CANCELED")
      case _: RunCompleted  => generateReport()
      case _                =>
      //    case RunStarting(ordinal, testCount, configMap, formatter, location, payload, threadName, timeStamp) =>
    }
  }

  private def scalaTestParser(name: String, testStatus: String): Array[StoryResult] = {
    val i = name.lastIndexOf(Separators.PIPE)

    val (testName, stories) =
      if (i >= 0) name.splitAt(i)
      else (name, s"${Separators.PIPE} None")

    stories
      .drop(1)                 // Drop the "|"
      .split(Separators.COMMA) // multiple stories
      .map(x => StoryResult(x.strip(), testName.strip(), testStatus))
  }

  private def javaTestParser(name: String, testStatus: String): Array[StoryResult] = {
    val i = name.lastIndexOf(Separators.__)

    val (testName, stories) =
      if (i >= 0) name.splitAt(i)
      else (name, s"${Separators.__} None")

    stories
      .drop(2)                      // Drop the "__"
      .split(Separators.UNDERSCORE) // multiple stories
      .sliding(2, 2)
      .map(x => x.mkString("-"))
      .map(x => StoryResult(x.strip(), testName.strip(), testStatus))
      .toArray
  }

  private def addResult(name: String, testStatus: String): Unit = {
    val storyResults = if (name.contains(Separators.PIPE)) scalaTestParser(name, testStatus)
    else javaTestParser(name, testStatus)

    results ++= storyResults
  }

  private val parentPath = "./target/RTM"
  private val reportFile = "/testStoryMapping.txt"

  private def generateReport(): Unit = {
    Files.createDirectories(new File(parentPath).toPath)
    val file = new FileWriter(parentPath + reportFile, true)

    // write to file
    results.foreach(x => file.append(x.format(Separators.PIPE) + Separators.NEWLINE))
    file.close()
  }
}
