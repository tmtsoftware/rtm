package tmt.test.reporter

import java.io.{File, FileWriter}
import java.nio.file.Files
import java.util.Calendar
import scala.jdk.CollectionConverters.CollectionHasAsScala
import tmt.test.reporter.Separators._
import scalatags.Text.all.{br, _}

object TestRequirementMapper {

  def main(args: Array[String]): Unit = {

    // read program parameters
    val (testResultsFile, requirementsFile, outputPath) = args.toList match {
      case t :: r :: o :: Nil => (t, r, o)
      case _                  =>
        throw new RuntimeException(
          "**** Provide appropriate parameters. **** \n " +
            "Required parameters : <file with test-story mapping> <file with story-requirement mapping> <output file>"
        )
    }

    // read test-story mapping
    val testResultsPath = new File(testResultsFile).toPath.toAbsolutePath
    println("[INFO] Reading test-story mapping file - " + testResultsPath)
    val testResults     = Files.readAllLines(new File(testResultsFile).toPath)
    val storyResults    = testResults.asScala.toList.map { line =>
      val (story, test, status) = line.split(PIPE).toList match {
        case s :: t :: st :: Nil => (s, t, st)
        case _                   =>
          throw new RuntimeException(
            s"**** Provided data is not in valid format : '$line' ****\n" +
              "Test-Story mapping should be in 'story number | test name | test status' format (Pipe '|' separated format)"
          )
      }
      StoryResult(story.strip(), test.strip(), status.strip())
    }

    // read story-requirement mapping
    println("[INFO] Reading story-requirement mapping file - " + new File(requirementsFile).toPath.toAbsolutePath)
    val requirementsContent = Files.readAllLines(new File(requirementsFile).toPath)

    val requirements = requirementsContent.asScala.toList.map { line =>
      val (story, requirement) = line.splitAt(line.indexOf(COMMA)) match {
        case (s, req) if s.nonEmpty => (s, req.drop(1)) // drop to remove the first comma & requirement can be empty.
        case _ =>
          throw new RuntimeException(
            s"**** Provided data is not in valid format : '$line' ****\n" +
              s"Story-Requirement mapping should be in 'story number $COMMA requirement' format (Comma ',' separated format)"
          )
      }

      Requirement(story.strip(), requirement.strip().replaceAll("\"", ""))
    }

    // map tests to requirements and sort by story ID
    val testAndReqMapped = storyResults.map { storyResult =>
      val correspondingReq = requirements
        .find(_.story == storyResult.story) // find the Requirements of given story
        .map(_.number)                      // take out the Requirement number
        .filter(_.nonEmpty)                 // remove if Requirement number is empty
        .getOrElse(Requirement.EMPTY)

        TestRequirementMapped(storyResult.story, correspondingReq, storyResult.test, storyResult.status)
      }
      .sortWith((a, b) => a.story.compareTo(b.story) < 0)

    val outputFile = new File(outputPath)
    val indexPath  = "/index.html"

    def createIndexFile(): Unit = {
      val writer         = new FileWriter(outputFile.getParent + indexPath)
      val testResultFile = testResultsPath.getFileName

      html(
        body(
          a(href := "./" + testResultFile, attr("download") := "")(testResultFile.toString),
          br(),
          a(href := "./" + outputFile.getName, attr("download") := "")(outputFile.getName),
          br(),
          a(href := "./" + outputFile.getName + ".html")("RTM HTML-report")
        )
      ).writeTo(writer)

      writer.close()
    }

    // create RTM in HTML format
    createHtmlReport(outputPath+ ".html", testAndReqMapped)

    // create index.html file
    createIndexFile()

    // write to csv-file
    println("[INFO] Writing results to - " + outputPath)
    Files.createDirectories(outputFile.getParentFile.toPath)
    createCsvFile(outputPath, testAndReqMapped)
    println(
      s"**** Successfully mapped Test results to Requirements **** : Check ${new File(outputPath).getCanonicalPath} for results"
    )
  }

  def createCsvFile(outputPath: String, testAndReqMapped: List[TestRequirementMapped]): Unit = {
    val writer = new FileWriter(outputPath)
    testAndReqMapped.map(result => result.format(PIPE) + NEWLINE).foreach(writer.write)
    writer.close()
  }

  def createHtmlReport(outputPath: String, testAndReqMapped: List[TestRequirementMapped]): Unit = {
    val writer         = new FileWriter(outputPath)
    val testAndReqGrouped = testAndReqMapped
      .filter(s => s.story.nonEmpty && s.story != Requirement.EMPTY)
      .groupBy(_.story)

    html(
      head(
        raw("<style>table, th, td {border: 1px solid black; border-collapse: collapse; }</style>")
      ),
      body(
        a(name := "toc"),
        h1("RTM report"),
        div("Generation time: ", Calendar.getInstance().getTime().toString),
        h2("Summary"),
        table(width := "50%")(
          tr(
            th("Story ID"),
            th("Requirements"),
            th(width := "10%")("Status")
          ),
          for ((storyId, testResults) <- testAndReqGrouped.toSeq) yield tr(
            td(
              a(href := "#" + storyId)(storyId)
            ),
            td(testResults(0).reqNum.replace(",", ", ")),
            if (testResults.count(t => t.status.toUpperCase == TestStatus.FAILED) > 0) td(color:="red")(TestStatus.FAILED)
            else if (testResults.count(t => t.status.toUpperCase != TestStatus.PASSED) > 0) td(color:="orange")(TestStatus.FAILED)
            else td(color:="green")(TestStatus.PASSED)
          )
        ),
        for ((storyId, testResults) <- testAndReqGrouped.toSeq) yield div(
          h3(
            a(name := storyId)(storyId)
          ),
          p("Requirements: ", testResults(0).reqNum.replace(",", ", ")),
          p(
            "JIRA link: ", a(href := "https://tmt-project.atlassian.net/browse/" + storyId, target := "_blank")(storyId)
          ),
          p("Tests:"),
          table(width := "50%")(
            tr(
              th("Test Name"),
              th(width := "10%")("Status")
            ),
            for (testRes <- testResults) yield tr(
              td(testRes.test),
              if (testRes.status.toUpperCase == TestStatus.FAILED) td(color:="red")(TestStatus.FAILED)
              else if (testRes.status.toUpperCase == TestStatus.PASSED) td(color:="green")(TestStatus.PASSED)
              else td(color:="orange")(testRes.status.toUpperCase)
            )
          ),
          p(
            a(href := "#toc")("back to top")
          ),
          hr(),
        ),
      )
    ).writeTo(writer)

    writer.close()
  }

}
