# Welcome to the TDS Regression Test Suite Application

The TDS-Regressions project contains a suite of regression tests for testing various tools, accommodations, and features in TDS.

The TDSRegressions test suite uses the JUnit test framework in conjunction with Spring to load and run the tests. All tests are written using
Selenium, a web browser automation test framework. By default, the TDS Regression test framework is built to utilize the Firefox WebDriver for all web interactions.

## Regression Tests Pre-conditions and Setup
The following steps are required in order to prepare the TDS-Regression test suite for execution.

### TDS Environment
* The Implementation Readiness Package (IRP), Practice, and Training Test packages must be loaded into TDS and ART.
* Run tdsregression_setup.sql, located in src/main/resources/scripts/ on the TDS MySQL database. This script does the following:
    * Updates IRP, Practice, Training assessment names to more unique, descriptive names that the regression test suite utilizes
    * Prepares the segment properties table
    * Adds accommodation and test tool seed data as required by many of the regression tests
* The tdsregression.properties file must be updated to include credentials for a proctor user, student user (grade 3), as well as the TDS student and proctor host URLs.



### Regression Test host environment and settings
The system hosting the regression test suite has the following dependencies:

  * Firefox version 45.0.2 (Compatible with Selenium 2.53.0)
  * MVN

If a different Firefox version is used, a compatible Selenium version must be defined in the Maven POM file.


### Maven dependencies
TDSRegressions has a number of direct dependencies. These dependencies are already built and included in the Maven POM file.

* selenium (2.53.0)
* log4j
* junit
* spring-test
* spring-core
* spring-context
* browsermob (proxy)
* zohhak

## Running the JUnit Test Suite
The TDS-Regressions JUnit test suite can be run both through an IDE or via the command line, using the "mvn test" command at the project root level.

On IntelliJ IDEA, the test suite can be ran by right clicking on the test package to run (or at the "/tests" package to run all tests) and clicking "Run Tests."

Additional arguments can be provided to specify which specific tests to run. Please see the documentation located [here](https://maven.apache.org/surefire/maven-surefire-plugin/examples/single-test.html)
for more information.

## Other Notes
Currently, all tools, accommodations, and designated supports are found in tests within IRP Test package with the exception of the "Global Notes" and "Dictionary" accommodation.
These two tools are found within the Grade 11 Performance test (from the "Practice Test" package).


## Links
* [Implementation Readiness Package (IRP)](ftp://ftps.smarterbalanced.org/~sbacpublic/Public/ImplementationReadiness/2015.08.19.IrpTestPackageAndContent.zip)
* [Practice Test Package](ftp://ftps.smarterbalanced.org/~sbacpublic/Public/PracticeAndTrainingTests/2015-08-28_PracticeTestPackagesAndContent.zip)
* [Training Test Package](ftp://ftps.smarterbalanced.org/~sbacpublic/Public/PracticeAndTrainingTests/2015-08-28_TrainingTestPackagesAndContent.zip)

## End to End API User Tests Pre-conditions and Setup
The following steps are required in order to prepare the end to end User testing for execution.

### Properties File

A properties file must include the following data:

* authenticateURI= (URI for OAuth access)
* userURI=(user URI for creating, updating and deleting a user)
* realm=(attribute to indicate the scope of protection)
* clientId=(client ID credential)
* clientSecret=(client secret credential for accessing a token)
* grantType=(client credentials for exchanging a password for an access token)
* password=(a password)
* username=(valid username)
* authenticateURIEnd=(OAuth URI ending)

Maven must be set up and a -D flag must be used to specify the location of the properties file. Example:
* mvn test -Dconfig=/pathto/config.properties
