import sbt._

object Version {
  val Flink            = "1.10.0"
  val Log4j2Version    = "2.11.1"
  val ScalaTest        = "3.1.1"
  val Hadoop           = "3.2.1"
  val EmbeddedKafka    = "2.5.0"
  val Avro             = "1.9.2"

}

object Dependencies {


  object Logging {
    lazy val log4jApi            = "org.apache.logging.log4j" % "log4j-api" % Version.Log4j2Version
    lazy val log4jSlf4j          = "org.apache.logging.log4j" % "log4j-slf4j-impl" % Version.Log4j2Version
    lazy val log4jCore           = "org.apache.logging.log4j" % "log4j-core" % Version.Log4j2Version
    lazy val Slf4j               = "com.typesafe.akka" %% "akka-slf4j" % "2.5.29"
    lazy val LoggingDependencies = Seq(log4jApi, log4jSlf4j, log4jCore, Slf4j)
  }

  object Testing {
    lazy val ScalaTest           = "org.scalatest" %% "scalatest" % Version.ScalaTest % Test
    lazy val TestingDependencies = Seq(ScalaTest)
  }

  object FlinkUtils {
    lazy val RocksDbStateBackend    = "org.apache.flink" %% "flink-statebackend-rocksdb" % Version.Flink
    lazy val FlinkHadoopFs          = "org.apache.flink" % "flink-hadoop-fs" % Version.Flink
    lazy val FlinkUtilsDependencies = Seq(RocksDbStateBackend, FlinkHadoopFs)
  }



  object Hadoop {
    lazy val Hdfs               = "org.apache.hadoop" % "hadoop-hdfs" % Version.Hadoop
    lazy val HadoopClient       = "org.apache.hadoop" % "hadoop-client" % Version.Hadoop
    lazy val HadoopCommon       = "org.apache.hadoop" % "hadoop-common" % Version.Hadoop
    lazy val HadoopDependencies = Seq(Hdfs, HadoopClient, HadoopCommon)
  }

}
