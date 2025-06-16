package tmt.test.reporter

import tmt.test.reporter.Separators.PIPE

import scala.io.Source

object RtmAggregator extends App {
  val aggregatedRtm = args.flatMap(projAndPath => {
    val (project, filePath) = projAndPath.split(":").toList match {
      case p :: f :: Nil => (p, f)
      case _ => throw new RuntimeException(
        s"**** Provided parameter is not in valid format : '$projAndPath' ****\n" +
          "RTM aggregator parameter should be in 'project:file' format"
      )
    }
    println("Project: " + project + "; Reading file: " + filePath)
    val fileSource = Source.fromFile(filePath)
    fileSource.getLines().map( line => {
      val (story, req, test, status) = line.split(PIPE).toList match {
        case s :: r :: t :: st :: Nil => (s.trim, r.trim, t.trim, st.trim)
        case _ =>
          throw new RuntimeException(
            s"**** Provided data ($filePath) is not in valid format : '$line' ****\n" +
              "RTM mapping should be in 'story number | requirement | test name | test status' format (Pipe '|' separated format)"
          )
      }

      // remove any extra text from story ID (and then append it to the test description)
      val (storyId, someText) = story.split(" ").toList match {
        case s :: Nil => (s, "")
        case s :: theRest => (s, theRest.mkString(" "))
        case Nil => (story, "")
      }

      TestRequirementMapped(storyId, req, project + ": " + test + " " + someText, status.toUpperCase)
    })
  }).toList
  TestRequirementMapper.createCsvFile("./aggregated.txt", aggregatedRtm)
  TestRequirementMapper.createHtmlReport("./aggregated.html", aggregatedRtm)
  println("**** Successfully aggregated and mapped Test results to Requirements ****")
}