package ru.omyagkov.external.kafka.example

import org.apache.flink.streaming.api.scala._
import cloudflow.flink.{FlinkStreamlet, FlinkStreamletLogic}
import cloudflow.streamlets.{ConfigParameter, StreamletShape}
import cloudflow.streamlets.avro.AvroOutlet
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.functions.co.CoProcessFunction
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector
import ru.omyagkov.ExampleObject
import ru.omyagkov.external.kafka.example.config.KafkaConfig

import scala.collection.immutable

class FlinkStreamletExample extends FlinkStreamlet with KafkaConfig {
  override def configParameters: immutable.IndexedSeq[ConfigParameter] =
    kafkaSettings

  @transient val exampleObject: AvroOutlet[ExampleObject] =
    AvroOutlet[ExampleObject]("example-out")

  override def shape(): StreamletShape =
    StreamletShape.withOutlets(exampleObject)
  override protected def createLogic(): FlinkStreamletLogic =
    new FlinkStreamletLogic() {
      override def buildExecutionGraph(): Unit = {
        val streamEnv = context.env
        streamEnv.setParallelism(1)

        val externalStreamIn = streamEnv.addSource(
          new FlinkKafkaConsumer[String](
            "topic",
            new SimpleStringSchema(),
            setKafkaConsumerProperty(
              context.streamletConfig.getString(kafkaBootstrapServers.key),
              context.streamletConfig.getString(kafkaConsumerGroupId.key)
            )
          )
        ).map(
          obj =>

            new ExampleObject(obj)
        )

        val generate =
          streamEnv.fromElements(ExampleObject("one"), ExampleObject("two"))
        //  generate.print("to producer")
        externalStreamIn
          .connect(generate)
          .process(new Coprocc())
          .addSink(
            flinkKafkaProducerSource[ExampleObject](
              setKafkaProducerProperty(
                context.streamletConfig.getString(kafkaBootstrapServers.key)
              ),
              context.streamletConfig.getString(kafkaTopicProducer.key)
            )
          )

        writeStream(exampleObject, externalStreamIn)

      }
    }

}
