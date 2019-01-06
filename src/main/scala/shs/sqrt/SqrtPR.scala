package shs.sqrt

import spinal.core._
import spinal.lib._

case class SqrtPR(r_width: Int = 16) extends Component {
  val v_width = 2 * r_width

  def valueType = UInt(v_width bits)

  def resultType = UInt(r_width bits)

  val io = new Bundle {
    val value = in(valueType)
    val rsp_value = out(valueType)
    val rsp_result = out(resultType)
  }

  def build(i: Int, value: UInt, acc: UInt, acc2: UInt): Unit = {
    if (i == 0) {
      io.rsp_value := value
      io.rsp_result := acc
      return
    }

    val l = i - 1
    val b = 1L << l
    val b2 = 1L << (l << 1)

    // `acc` holds the accumulated result, and `acc2` is
    //  the accumulated square of the accumulated result.
    val guess = resultType
    guess := acc | U(b)
    val guess2 = valueType
    guess2 := acc2 + U(b2) + ((acc << l) << 1)

    val hit = guess2 <= value

    build(i - 1,
      RegNext(value) init 0,
      RegNext(hit ? guess | acc) init 0,
      RegNext(hit ? guess2 | acc2) init 0)
  }

  build(r_width, io.value, U(0, r_width bits), U(0, v_width bits))
}
