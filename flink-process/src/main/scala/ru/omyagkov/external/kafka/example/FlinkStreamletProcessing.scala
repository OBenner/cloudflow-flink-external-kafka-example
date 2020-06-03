package ru.omyagkov.external.kafka.example

import org.apache.flink.streaming.api.scala._
import cloudflow.flink.{FlinkStreamlet, FlinkStreamletLogic}
import cloudflow.streamlets.StreamletShape
import cloudflow.streamlets.avro.AvroInlet
import ru.omyagkov.ExampleObject

class FlinkStreamletProcessing  extends FlinkStreamlet {
  @transient val exampleObjectIN: AvroInlet[ExampleObject] =
    AvroInlet[ExampleObject]("example-in")
  override def shape(): StreamletShape = StreamletShape.withInlets(exampleObjectIN)
  override protected def createLogic(): FlinkStreamletLogic = new FlinkStreamletLogic() {
    override def buildExecutionGraph(): Unit = {

      val packageToProgram = readStream(exampleObjectIN)
      packageToProgram.print("from inlet")
    }
  }

}
