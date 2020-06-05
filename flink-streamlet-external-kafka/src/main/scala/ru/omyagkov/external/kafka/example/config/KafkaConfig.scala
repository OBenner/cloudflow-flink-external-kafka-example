package ru.omyagkov.external.kafka.example.config

import java.util.Properties

import cloudflow.streamlets.{BooleanConfigParameter, StringConfigParameter}
import org.apache.avro.specific.SpecificRecordBase
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaConsumerBase, FlinkKafkaProducer}
import ru.omyagkov.external.kafka.example.utils.{FlinkKafkaBijectionDeserializer, FlinkKafkaBijectionSerializer}

import scala.reflect.ClassTag

trait KafkaConfig {
  protected val kafkaBootstrapServers: StringConfigParameter =
    StringConfigParameter(
      "kafka-bootstrap-servers",
      "Host:Port of external Kafka",
      Some("localhost:9092")
    )

  protected val kafkaConsumerGroupId: StringConfigParameter =
    StringConfigParameter(
      "kafka-consumer-group-id",
      "A unique string that identifies the consumer group this consumer belongs to",
      Some("flink-streamlet-external-kafka")
    )

  protected val kafkaTopicConsumer: StringConfigParameter =
    StringConfigParameter(
      "kafka-topic-consumer",
      "Listening Topic",
      Some("test")
    )
  protected val kafkaTopicProducer: StringConfigParameter =
    StringConfigParameter(
      "kafka-topic-producer",
      "Listening Topic",
      Some("test")
    )
  protected val enableStartFromEarliestKafkaPosition: BooleanConfigParameter =
    BooleanConfigParameter(
      "start-earliest-position",
      "StartFromEarliestKafkaPosition",
      Some(true)
    )

  val kafkaSettings =
    Vector(
      kafkaBootstrapServers,
      kafkaConsumerGroupId,
      kafkaTopicConsumer,
      kafkaTopicProducer,
      enableStartFromEarliestKafkaPosition
    )

  protected def flinkKafkaProducerSource[A <: SpecificRecordBase : ClassTag](
                                                                              kafkaProperty: Properties,
                                                                              kafkaTopic: String
                                                                            ): FlinkKafkaProducer[A] = {

    val producer = new FlinkKafkaProducer[A](
      kafkaTopic,
      new FlinkKafkaBijectionSerializer[A](kafkaTopic),
      kafkaProperty,
      FlinkKafkaProducer.Semantic.EXACTLY_ONCE
    )
    producer
  }

  protected def flinkKafkaConsumerSource[A <: SpecificRecordBase : ClassTag](
                                                                              kafkaProperty: Properties,
                                                                              kafkaTopic: String,
                                                                              enableStartFromEarliestKafkaPosition: Boolean
                                                                            ): FlinkKafkaConsumerBase[A] = {

    val consumer = new FlinkKafkaConsumer[A](
      kafkaTopic,
      new FlinkKafkaBijectionDeserializer[A],
      kafkaProperty
    )
    if (enableStartFromEarliestKafkaPosition) consumer.setStartFromEarliest()
    consumer
  }

  protected def setKafkaConsumerProperty(kafkaServer: String,
                                         groupId: String): Properties = {
    val props = new Properties()
    props.setProperty("bootstrap.servers", kafkaServer)
    props.setProperty("group.id", groupId)
    props
  }

  protected def setKafkaProducerProperty(kafkaServer: String): Properties = {
    val props = new Properties()
    props.setProperty("bootstrap.servers", kafkaServer)
    props.setProperty("enable.idempotence", "true")
   // props.put("transactional.id", "r1")
    props.setProperty("transaction.timeout.ms", "900000")
    props
  }
}
