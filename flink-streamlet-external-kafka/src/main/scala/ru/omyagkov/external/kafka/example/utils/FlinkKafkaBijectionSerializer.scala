package ru.omyagkov.external.kafka.example.utils

import java.lang

import com.twitter.bijection.Injection
import com.twitter.bijection.avro.SpecificAvroCodecs
import org.apache.avro.specific.SpecificRecordBase
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema
import org.apache.kafka.clients.producer.ProducerRecord

import scala.reflect.ClassTag

class FlinkKafkaBijectionSerializer[A <: SpecificRecordBase: ClassTag](val kafkaTopic: String)  extends KafkaSerializationSchema[A]{
  override def serialize(element: A, timestamp: lang.Long): ProducerRecord[Array[Byte], Array[Byte]] = {

    val recordInjection: Injection[A, Array[Byte]] =
      SpecificAvroCodecs.toBinary(element.getSchema)
    new ProducerRecord[Array[Byte], Array[Byte]](kafkaTopic, recordInjection(element))
  }
}
