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

  def loop(i: Int, value: UInt, acc: UInt, acc2: UInt): Unit = {
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

    val next_value = Reg(valueType) init 0
    val next_acc = Reg(resultType) init 0
    val next_acc2 = Reg(valueType) init 0

    next_value := value
    when(guess2 <= value) {
      next_acc := guess
      next_acc2 := guess2
    } otherwise {
      next_acc := acc
      next_acc2 := acc2
    }

    loop(i - 1, next_value, next_acc, next_acc2)
  }

  loop(r_width, io.value, U(0, r_width bits), U(0, v_width bits))
}
