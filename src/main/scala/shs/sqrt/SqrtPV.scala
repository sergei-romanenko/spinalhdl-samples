package shs.sqrt

// This example is based on
// http://iverilog.wikia.com/wiki/Simulation

import spinal.core._
import spinal.lib._

// Generating a pipeline...

case class SqrtPV(r_width: Int) extends Component {
  val v_width = 2 * r_width

  def valueType = UInt(v_width bits)

  def resultType = UInt(r_width bits)

  val io = new Bundle {
    val value = in(valueType)
    val rsp_value = out(valueType)
    val rsp_result = out(resultType)
  }

  val value = Vec(Reg(valueType) init 0, r_width + 1)
  val acc = Vec(Reg(resultType) init 0, r_width + 1)
  val acc2 = Vec(Reg(valueType) init 0, r_width + 1)

  value(0) := io.value
  acc(0) := 0
  acc2(0) := 0

  io.rsp_value := value(r_width)
  io.rsp_result := acc(r_width)

  for (l <- (r_width - 1) to 0 by -1) {
    val i = r_width - l
    val b = 1L << l
    val b2 = 1L << (l << 1)
    val guess = resultType
    guess := acc(i - 1) | U(b)
    val guess2 = valueType
    guess2 := acc2(i - 1) + U(b2) + ((acc(i - 1) << l) << 1)

    value(i) := value(i - 1)
    when(guess2 <= value(i - 1)) {
      acc(i) := guess
      acc2(i) := guess2
    } otherwise {
      acc(i) := acc(i - 1)
      acc2(i) := acc2(i - 1)
    }
  }
}
