
name := "cloudflow-flink-external-kafka-example"
ThisBuild / scalaVersion := Settings.ScalaVersion.It
version := "0.1"

val appName = "cloudflow-flink-external-kafka-example"

lazy val root = Project(id = appName, base = file("."))
  .settings(
    name := appName
  )
  .aggregate(
    pipeline,
    flinkStreamletExternalKafka
  )

lazy val pipeline = appModule("pipeline")
  .enablePlugins(CloudflowApplicationPlugin)
  .settings(
    blueprint := Some("blueprint.conf"),
    runLocalConfigFile := Some((baseDirectory.value / "src/main/resources/local.conf").getAbsolutePath),
    libraryDependencies += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.11.0", // for success embedded kafka start
    libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind"    % "2.11.0"  // for success embedded kafka start
  )
  .dependsOn(
    flinkStreamletExternalKafka
  )

lazy val flinkStreamletExternalKafka = appModule("flink-streamlet-external-kafka")
  .enablePlugins(CloudflowFlinkLibraryPlugin)
  .settings(
    libraryDependencies ++= Dependencies.FlinkUtils.FlinkUtilsDependencies,
    libraryDependencies ++= Dependencies.Testing.TestingDependencies,
    libraryDependencies ++= Dependencies.Hadoop.HadoopDependencies
  )
  .dependsOn()
def appModule(moduleID: String): Project = {
  Project(id = moduleID, base = file(moduleID))
    .settings(name := moduleID)
    .settings(Settings.commonSettings, libraryDependencies ++= Dependencies.Logging.LoggingDependencies)
}