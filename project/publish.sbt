ThisBuild / sonatypeProfileName := "io.github.oskin1"

ThisBuild / publishTo := sonatypePublishToBundle.value

ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"

ThisBuild / versionScheme := Some("early-semver")

ThisBuild / licenses := Seq("CC0 1.0 Universal" -> url("https://github.com/oskin1/rocks4cats/blob/master/LICENSE"))

ThisBuild / homepage := Some(url("https://github.com/oskin1/rocks4cats"))

ThisBuild / publishMavenStyle := true

Test / publishArtifact := false

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