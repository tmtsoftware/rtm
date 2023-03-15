# TMT Test reporters

### Build setup for Test reporter
Add following line to build.sbt

```
libraryDependencies += "com.github.tmtsoftware"  %% "rtm" % "0.2.0"
```


1. For Scala tests

    ```
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF", "-C", "tmt.test.reporter.TestReporter")
    ```
2. For Java tests

    ```
    testOptions in Test += Tests.Argument(TestFrameworks.JUnit)
    ```
3. For Kotlin tests

    - Add `org.junit.jupiter.api.extension.Extension` file at this path `src/test/resources/META-INF/services/`
    - Add following line in this file 
    ```
    tmt.test.reporter.KtTestReporter
    ```
    - Add `junit-platform.properties` file at path `src/test/resources/`
    - Add following properties file to enable autodetection of extension
    
    ```
    junit.jupiter.testinstance.lifecycle.default = per_class
    junit.jupiter.extensions.autodetection.enabled = true
    ```

Test-Story mapping will be generated in file `./target/RTM/testStoryMapping.txt` 
To Override the output path set env `RTM_PATH=/output-path/`

### Output format for testStoryMapping.txt
```
STORY-NUMBER1 | TEST-NAME1 | PASSED
```

## Required format of test name

1. For Scala and Kotlin tests 

    Test name should be followed by pipe (`|`) and then comma separated story id's.

    For ex.
    ```
    dummy test name | story-id-1, story-id-2
    ```
2. For Java tests

    Test name should be followed by two underscores (`__`) and then story id's separated using single underscore(`_`)

    For single story id :
    ```
    dumyTestName__DEOPSCSW_storyId1
    ```
    For multiple story id's :
    ```
    dumyTestName__DEOPSCSW_storyId1_DEOPSCSW_storyId2
    ```

# TMT Requirement Test Mapper (aka RTM)


## Prerequisites 
- Story-Requirement mapping from JIRA.
    The Story-Requirement mappings file need to be in the CSV format like following-
    ```
    STORY-NUMBER1,REQUIREMENT-NUMBER1
    STORY-NUMBER2,REQUIREMENT-NUMBER2,REQUIREMENT-NUMBER3
    ```
- Test-Story reports generated using TMT test reporters.
    Reports will be generated in the following format -
    ```
    STORY-NUMBER1 | TEST-NAME1 | PASSED
    STORY-NUMBER2 | TEST-NAME2 | FAILED
    ```

## To generate RTM-reports

Call the TestRequirementMapper from the bash shell by executing command with following arguments
- test-story mapping file path (generated using test reporter)
- story requirement mapping file path (as per above requirements)
- output path : `./target/RTM/output.txt`
```
> coursier launch --channel https://raw.githubusercontent.com/tmtsoftware/osw-apps/master/apps.json rtm:0.2.0 -- <path of file containing Test-Story mapping > <path of file containing Story-Requirement mapping> <output path>
```

## To aggregate several RTM-reports

For some projects (for example ESW), the final RTM will consist of several RTMs from subprojects. 
There is a special command to aggregate many RTM-reports into one, i.e. for ESW it will look like:
```shell
sbt "runMain tmt.test.reporter.RtmAggregator esw:path/to/rtm.txt esw-ts:path/to/rtm.txt esw-ocs-eng-ui:path/to/rtm.txt esw-observing-simulation:path/to/rtm.txt"
```
Here, we are aggregating RTM-reports from four subproject into one. Each RTM-report is specified as a parameter (for example `esw:path/to/rtm.txt`) with the format:
```shell
<sub-project>:<path-to-rtm>
```
Where:
- `<sub-project>` some unique tag that you want to use in the aggregated RTM-report for the given subproject
- `<path-to-rtm>` path to RTM-report for the given subproject

As an output of the command, there will be created two files in the current folder:
- `aggregated.txt` - aggregated RTM-report in CSV format
- `aggregated.html` - aggregated RTM-report in HTML format
