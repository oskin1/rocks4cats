name := "rocks4cats"

version := "0.0.2"

organization := "io.github.oskin1"

lazy val scala212 = "2.12.14"
lazy val scala213 = "2.13.6"
lazy val supportedScalaVersions = List(scala213, scala212)

scalaVersion := scala213
crossScalaVersions := supportedScalaVersions

libraryDependencies ++= List(
  "org.typelevel" %% "cats-effect" % "2.5.3",
  "org.rocksdb"    % "rocksdbjni"  % "6.22.1"
)
