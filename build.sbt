import sbt.Credentials




lazy val commonSettings = Seq(

  organization := "org.khariri",
  version := "1.0",
  name := "scala-play-basic",
  scalaVersion := "2.11.8"
)

lazy val root = (project in file(".")).settings(commonSettings: _*).settings(
  pipelineStages := Seq(digest, gzip),

    libraryDependencies ++= Seq(
    jdbc,
    cache,
    ws,
    "mysql" % "mysql-connector-java" % "6.0.5",
    "org.apache.hive" % "hive-jdbc" % "2.0.0",
     "fastutil" % "fastutil" % "5.0.9",
      "com.github.martincooper" %% "scala-datatable" % "0.7.0",
      "com.madhukaraphatak" %% "java-sizeof" % "0.1"
  )

).enablePlugins(PlayScala)