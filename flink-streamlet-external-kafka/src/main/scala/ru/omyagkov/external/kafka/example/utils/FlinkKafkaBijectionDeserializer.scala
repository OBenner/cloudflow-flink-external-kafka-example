package ru.omyagkov.external.kafka.example.utils

import cloudflow.streamlets.DecodeException
import cloudflow.streamlets.avro.AvroUtil
import com.twitter.bijection.avro.SpecificAvroCodecs
import org.apache.avro.specific.SpecificRecordBase
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.typeutils.TypeExtractor
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema
import org.apache.kafka.clients.consumer.ConsumerRecord

import scala.reflect.{ClassTag, _}
import scala.util.Failure


class FlinkKafkaBijectionDeserializer[A <: SpecificRecordBase: ClassTag] extends KafkaDeserializationSchema[A] {

  override def isEndOfStream(nextElement: A): Boolean = false

  override def deserialize(record: ConsumerRecord[Array[Byte], Array[Byte]]): A = {
    val schema = AvroUtil.makeSchema[A]
    SpecificAvroCodecs
      .toBinary[A](schema)
      .invert(record.value())
      .recoverWith {
        case ex ⇒
          Failure(DecodeException("Could not deserialize, cause: ", ex))
      }
      .get
  }

  override def getProducedType: TypeInformation[A] = {
    TypeExtractor
      .getForClass(classTag[A].runtimeClass.asInstanceOf[Class[A]])
      .asInstanceOf[TypeInformation[A]]
  }

}