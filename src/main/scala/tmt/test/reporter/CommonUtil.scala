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
}
