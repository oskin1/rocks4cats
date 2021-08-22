name := "rocks4cats"

version := "0.0.1"

organization := "com.github.oskin1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "2.5.3",
  "org.rocksdb"    % "rocksdbjni"  % "6.22.1"
)

licenses in ThisBuild := Seq("CC0 1.0 Universal" -> url("https://github.com/oskin1/rocks4cats/blob/master/LICENSE"))

homepage in ThisBuild := Some(url("https://github.com/oskin1/rocks4cats"))

publishMavenStyle in ThisBuild := true

publishArtifact in Test := false

publishTo in ThisBuild :=
  Some(if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging)

pomExtra in ThisBuild :=
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
