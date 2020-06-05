package ru.omyagkov.external.kafka.example

import org.apache.flink.api.common.state.{MapState, MapStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.co.CoProcessFunction
import org.apache.flink.util.Collector
import ru.omyagkov.ExampleObject
import org.apache.flink.api.scala.createTypeInformation

class CoProcc
    extends CoProcessFunction[ExampleObject, ExampleObject, ExampleObject] {
  private lazy val stateDescriptor: MapStateDescriptor[String, ExampleObject] =
    new MapStateDescriptor[String, ExampleObject](
      "state",
      createTypeInformation[String],
      createTypeInformation[ExampleObject]
    )
  private var state: MapState[String, ExampleObject] = _

  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    state = getRuntimeContext.getMapState(stateDescriptor)
  }
  override def processElement1(
    value: ExampleObject,
    ctx: CoProcessFunction[ExampleObject, ExampleObject, ExampleObject]#Context,
    out: Collector[ExampleObject]
  ): Unit = {
    state.put("1", null)
    state.get("2").field
    out.collect(value)
  }

  override def processElement2(
    value: ExampleObject,
    ctx: CoProcessFunction[ExampleObject, ExampleObject, ExampleObject]#Context,
    out: Collector[ExampleObject]
  ): Unit = {
    state.put("1", null)
    state.get("2").field
    out.collect(value)
  }
}
