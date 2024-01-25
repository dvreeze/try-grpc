
val scalaVer = "3.3.1"
val crossScalaVer = Seq(scalaVer)

ThisBuild / description := "Trying out gRPC"
ThisBuild / organization := "eu.cdevreeze.trygrpc"
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / versionScheme := Some("strict")

ThisBuild / scalaVersion := scalaVer
ThisBuild / crossScalaVersions := crossScalaVer

ThisBuild / semanticdbEnabled := false // do not enable SemanticDB

ThisBuild / scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

ThisBuild / publishMavenStyle := true

ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) {
    Some("snapshots".at(nexus + "content/repositories/snapshots"))
  } else {
    Some("releases".at(nexus + "service/local/staging/deploy/maven2"))
  }
}

// TODO versionScheme, such as early-semver

ThisBuild / pomExtra := pomData
ThisBuild / pomIncludeRepository := { _ => false }

val grpcVersion = "1.61.0"

ThisBuild / libraryDependencies += "io.grpc" % "grpc-netty-shaded" % grpcVersion % Runtime

ThisBuild / libraryDependencies += "io.grpc" % "grpc-protobuf" % grpcVersion

ThisBuild / libraryDependencies += "io.grpc" % "grpc-stub" % grpcVersion

ThisBuild / libraryDependencies += "io.grpc" % "grpc-services" % grpcVersion

ThisBuild / libraryDependencies += "org.apache.tomcat" % "annotations-api" % "6.0.53" % Provided

ThisBuild / libraryDependencies += "com.google.protobuf" % "protobuf-java" % "3.22.2" % "protobuf"

ThisBuild / libraryDependencies += "com.google.protobuf" % "protobuf-java-util" % "3.22.2" % "protobuf"

ThisBuild / libraryDependencies += ("io.grpc" % "protoc-gen-grpc-java" % "1.53.0").asProtocPlugin()

Compile / PB.targets := Seq(
  PB.gens.java("3.22.2") -> (Compile / sourceManaged).value,
  PB.gens.plugin("grpc-java") -> (Compile / sourceManaged).value,
)

val circeVersion = "0.14.6"

ThisBuild / libraryDependencies += "io.circe" %% "circe-core" % circeVersion
ThisBuild / libraryDependencies += "io.circe" %% "circe-generic" % circeVersion
ThisBuild / libraryDependencies += "io.circe" %% "circe-parser" % circeVersion

ThisBuild / libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % Test

lazy val root = project
  .in(file("."))
  .settings(
    name := "trygrpc"
  )

lazy val pomData =
  <url>https://github.com/dvreeze/try-grpc</url>
    <licenses>
      <license>
        <name>Apache License, Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
        <distribution>repo</distribution>
        <comments>Try-grpc is licensed under Apache License, Version 2.0</comments>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:git@github.com:dvreeze/try-grpc.git</connection>
      <url>https://github.com/dvreeze/try-grpc.git</url>
      <developerConnection>scm:git:git@github.com:dvreeze/try-grpc.git</developerConnection>
    </scm>
    <developers>
      <developer>
        <id>dvreeze</id>
        <name>Chris de Vreeze</name>
        <email>chris.de.vreeze@caiway.net</email>
      </developer>
    </developers>
