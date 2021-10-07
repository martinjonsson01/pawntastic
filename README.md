# Pawntastic
An object-oriented game written in Java using libGDX.

<!-- Badge showing test suite status -->
[![Test Suite](https://github.com/martinjonsson01/OOPP-WITH-THE-BOIS/actions/workflows/test.yml/badge.svg?branch=master)](https://github.com/martinjonsson01/OOPP-WITH-THE-BOIS/actions/workflows/test.yml)

## Understanding the Architecture
There is a UML class diagram of each package located in the README file of each package directory. It is visible directly when navigating to the package directory on GitHub, but if viewing the files locally the raw images can be found in `documents/diagrams/`.

![com.thebois package class diagram](documents/diagrams/com.thebois.jpg "com.thebois package class diagram")

## Running the Game

### Prerequisites

* [Java 15](https://jdk.java.net/archive/) or later

To run the latest version of the game, download the jar file from [releases](https://github.com/martinjonsson01/OOPP-WITH-THE-BOIS/releases/) and run it using the command:
```terminal
java -jar ColonyManagement-vX.X.X.jar
```

## Running From Source

To build the game directly from the source code, you need to have some common development tools installed.

### Prerequisites

* [JDK 15](https://jdk.java.net/archive/)
* [Maven](http://maven.apache.org/install.html) installed according to instructions
* JAVA_HOME environment variable set to point to JDK 15

### How to Build and Run From Source

1. Clone the repository using `git clone https://github.com/martinjonsson01/OOPP-WITH-THE-BOIS.git` or downloading as ZIP.
2. Navigate to the root directory: `cd OOPP-WITH-THE-BOIS`
3. Run `mvn clean test` to download runtime dependencies and test dependencies, and to check that the program is running correctly.
4. Run `mvn compile exec:java` to start the program.

### Common Issues and Solutions
* You get `java.lang.UnsupportedClassVersionError`.
  * You're running the program with too old a version of Java. You need Java 15 or later. If you have it installed, then your environment variables are incorrectly configured. Make sure your Path environment variable contains a path to the `bin` folder of your Java installation. Also make sure that no older versions of Java are in your Path (both user and system variables)
* `mvn` not recognized as a command.
  * The `bin` folder of the Maven installation has not been added to the PATH environment variable.
* `mvn` outputs *"The JAVA_HOME environment variable is not defined correctly"*
  * The environment variable `JAVA_HOME` in Windows is not set to the path of the installed JDK 15 (should **not** be the bin folder).
* Not using the correct JDK version.
  * Double check the version by running `java --version`. It should be `15.0.2` or later. If it is incorrect, update your `JAVA_HOME` and/or `PATH` environment variables.