name := "rocks4cats"

version := "0.1.0"

organization := "io.github.oskin1"

lazy val scala212               = "2.12.14"
lazy val scala213               = "2.13.6"
lazy val supportedScalaVersions = List(scala213, scala212)

scalaVersion := scala213
crossScalaVersions := supportedScalaVersions

libraryDependencies ++= List(
  "org.typelevel" %% "cats-effect" % "2.5.3",
  "org.rocksdb"    % "rocksdbjni"  % "6.22.1"
)

ThisBuild / sonatypeProfileName := "io.github.oskin1"
ThisBuild / publishTo := sonatypePublishToBundle.value
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / licenses := Seq("CC0 1.0 Universal" -> url("https://github.com/oskin1/rocks4cats/blob/master/LICENSE"))
ThisBuild / homepage := Some(url("https://github.com/oskin1/rocks4cats"))
ThisBuild / publishMavenStyle := true
ThisBuild / pomExtra :=
  <scm>
    <url>git@github.com:oskin1/rocks4cats.git</url>
    <connection>scm:git:git@github.com:oskin1/rocks4cats.git</connection>
  </scm>
    <developers>
      <developer>
        <id>Oskin1</id>
        <name>Ilya Oskin</name>
      </developer>
    </developers>

Test / publishArtifact := false
