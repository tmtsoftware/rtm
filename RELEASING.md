### Steps to release rtm:

1. Update `README.md` with the version to be released and commit the changes.
1. Change release version in `build.sbt`. For example, if you want to release version 1.0.0
   then add `version := "1.0.0"` to `build.sbt`
1. Run `release.sh $VERSION$` script by providing version number argument.
   **Note:** `PROD=true` environment variable needs to be set before running `release.sh`
