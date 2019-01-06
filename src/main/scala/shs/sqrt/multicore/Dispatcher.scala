package shs.sqrt.multicore

import spinal.core._
import spinal.lib._

// This class is taken from
// https://github.com/SpinalHDL/SpinalWorkshop

case class Dispatcher[T <: Data](dataType: T, outputsCount: Int)
  extends Component {

  val io = new Bundle {
    val input = slave Stream dataType
    val outputs = Vec(master Stream dataType, outputsCount)
  }

  val counter = Counter(io.outputs.length)

  for (output <- io.outputs) {
    output.valid := False
    output.payload := io.input.payload
  }

  io.outputs(counter.value).valid := io.input.valid
  io.input.ready := io.outputs(counter).ready

  when(io.input.fire) {
    counter.increment()
  }
}
