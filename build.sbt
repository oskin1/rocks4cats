import sbt.Keys.{crossScalaVersions, version}
import xerial.sbt.Sonatype.GitHubHosting

lazy val scala213               = "2.13.12"
lazy val supportedScalaVersions = List(scala213)

lazy val commonSettings = List(
  name := "rocks4cats",
  version := "0.2.2",
  organization := "io.github.oskin1",
  scalaVersion := scala213
)

lazy val publishingSettings = List(
  sonatypeProfileName := "io.github.oskin1",
  publishTo := sonatypePublishToBundle.value,
  sonatypeCredentialHost := "s01.oss.sonatype.org",
  versionScheme := Some("semver-spec"),
  licenses := Seq("CC0 1.0 Universal" -> url("https://github.com/oskin1/rocks4cats/blob/master/LICENSE")),
  homepage := Some(url("https://github.com/oskin1/rocks4cats")),
  publishArtifact := true,
  publishMavenStyle := true,
  Test / publishArtifact := false,
  sonatypeProjectHosting := Some(GitHubHosting("oskin1", "rocks4cats", "ilya.arcadich@gmail.com"))
)

lazy val rocks4cats = project
  .in(file("."))
  .withId("rocks4cats")
  .settings(commonSettings ++ publishingSettings)
  .settings(moduleName := "rocks4cats", publishArtifact := false)
  .aggregate(core, scodec)

lazy val core = project
  .in(file("core"))
  .withId("rocks4cats-core")
  .settings(moduleName := "rocks4cats-core")
  .settings(commonSettings ++ publishingSettings)
  .settings(
    libraryDependencies ++= List(
      "org.typelevel" %% "cats-effect" % "3.4.2",
      "org.rocksdb"    % "rocksdbjni"  % "7.0.3"
    )
  )

lazy val scodec = project
  .in(file("scodec"))
  .withId("rocks4cats-scodec")
  .settings(moduleName := "rocks4cats-scodec")
  .settings(commonSettings ++ publishingSettings)
  .settings(
    libraryDependencies ++= List(
      "org.scodec" %% "scodec-core" % "1.11.7",
      "org.scodec" %% "scodec-bits" % "1.1.21"
    )
  )
  .dependsOn(core % allConfigDependency)

lazy val allConfigDependency = "compile->compile;test->test"
