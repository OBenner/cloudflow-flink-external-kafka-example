import sbt.Keys._
import sbt._

object Settings {

  object ScalaVersion {
    val Major = "2.12"
    val It    = s"$Major.11"
  }

  lazy val commonSettings = Seq(
    scalaVersion := ScalaVersion.It,
    scalacOptions := CompileOptions.compileOptions,
    scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value
  )

  // Add this for suppress warnings in datamodel module, maybe can change this in future
  lazy val suppressWarnSettings = Seq(
    scalacOptions --= Seq(
      "-Ywarn-dead-code",
      "-Ywarn-unused",
      "-Ywarn-unused:implicits",
      "-Ywarn-unused:imports",
      "-Ywarn-unused:locals",
      "-Ywarn-unused:params",
      "-Ywarn-unused:patvars",
      "-Ywarn-unused:privates",
      "-Xlint"
    )
  )

}
