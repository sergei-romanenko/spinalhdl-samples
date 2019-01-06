package shs.sqrt.multicore

import spinal.core._
import spinal.lib._

// This class is taken from
// https://github.com/SpinalHDL/SpinalWorkshop

case class Arbiter[T <: Data](dataType: T, inputsCount: Int) extends Component {
  val io = new Bundle {
    val inputs = Vec(slave Stream dataType, inputsCount)
    val output = master Stream dataType
  }
  val counter = Counter(io.inputs.length)

  for (input <- io.inputs) {
    input.ready := False
  }

  io.inputs(counter.value).ready := io.output.ready
  io.output.valid := io.inputs(counter).valid
  io.output.payload := io.inputs(counter).payload

  when(io.output.fire) {
    counter.increment()
  }
}
