name := "sclera-plugin-csv-lite"

description := "Example add-on package, enables Sclera to work with CSV data"

version := "1.0-SNAPSHOT"

scalaVersion := "2.13.1"

licenses := Seq("Apache License version 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt"))

libraryDependencies ++= Seq(
    "org.apache.commons" % "commons-csv" % "1.8",
    "com.scleradb" %% "sclera-tools" % "4.0" % "provided",
    "com.scleradb" %% "sclera-core" % "4.0" % "provided",
    "com.scleradb" %% "sclera-config" % "4.0" % "test",
    "org.scalatest" %% "scalatest" % "3.1.1" % "test"
)

scalacOptions ++= Seq(
    "-Werror", "-feature", "-deprecation", "-unchecked"
)

exportJars := true

javaOptions in Test ++= Seq(
    s"-DSCLERA_ROOT=${java.nio.file.Files.createTempDirectory("scleratest")}"
)

fork in Test := true
