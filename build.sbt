name := "rocks4cats"

version := "0.0.1"

organization := "io.github.oskin1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "2.5.3",
  "org.rocksdb"    % "rocksdbjni"  % "6.22.1"
)
