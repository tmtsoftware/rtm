package tmt.test.reporter

import java.io.{File, FileWriter}
import java.nio.file.Files

object CommonUtil {

  def generateReport(parentPath: String, reportFile: String, results: Iterable[StoryResult]): Unit = {
    Files.createDirectories(new File(parentPath).toPath)
    val file = new FileWriter(parentPath + reportFile, true)

    // write to file
    results.foreach(x => file.append(x.format(Separators.PIPE) + Separators.NEWLINE))
    file.close()
  }

  def getTestData(name: String, separator: String): (String, String) = {
    val i = name.lastIndexOf(separator)
    if (i >= 0) name.splitAt(i) else (name, s"${separator} None")
  }

  def javaTestParser(name: String, testStatus: String): Array[StoryResult] = {
    val (testName, stories) = CommonUtil.getTestData(name, Separators.__)

    // handle the data provider case (see CSW EventSubscriberTest for an example)
    // if there is a (, take it out of stories list and append to test name
    val (newStories, dataProvider) = stories.indexOf("(") match {
      case -1  => (stories, "")
      case pos => (stories.take(pos), stories.takeRight(stories.length - pos))
    }

    newStories
      .drop(2)                      // Drop the "__"
      .split(Separators.UNDERSCORE) // multiple stories
      .sliding(2, 2)
      .map(x => x.mkString("-"))
      .map(x => StoryResult(x.strip(), testName.strip() + dataProvider, testStatus))
      .toArray
  }
}
