package shs.sqrt

// This example is based on
// http://iverilog.wikia.com/wiki/Simulation

import spinal.core._
import spinal.lib._

case class SqrtGenerics(r_width: Int = 16) {
  val v_width = r_width * 2

  def valueType = UInt(v_width bits)
  def resultType = UInt(r_width bits)
}

case class SqrtTask(g: SqrtGenerics) extends Bundle {
  val value = g.valueType
}

case class SqrtResult(g: SqrtGenerics) extends Bundle {
  val value = g.valueType
  val result = g.resultType
}


case class Sqrt(g: SqrtGenerics) extends Component {
  val io = new Bundle {
    val cmd = slave Stream SqrtTask(g)
    val rsp = master Stream SqrtResult(g)
  }

  import g._

  // Keep track of which bit I'm working on.
  val lw = log2Up(r_width)
  val l = UInt(lw bits)
  val i = Reg(UInt(lw + 1 bits))
  l := (i - 1) (0 until lw)

  val b = resultType
  b := U(1) << l
  val sq_b = valueType
  sq_b := U(1) << (l << 1)

  // `acc` holds the accumulated result, and `acc2` is
  //  the accumulated square of the accumulated result.
  val acc = Reg(resultType)
  val acc2 = Reg(valueType)

  // guess holds the potential next values for acc,
  // and guess2 holds the square of that guess.
  val guess = resultType
  guess := acc | b
  val guess2 = valueType
  guess2 := acc2 + sq_b + ((acc << l) << 1)

  def clear(): Unit = {
    io.cmd.ready := True
    acc := 0
    acc2 := 0
    i := U(r_width)
  }

  //Apply default assignement
  io.rsp.value := io.cmd.value
  io.rsp.result := acc
  io.cmd.ready := False
  io.rsp.valid := False

  when(io.cmd.valid) {
    //Is the sqrt iteration done?
    when(i =/= 0) {
      i := i - 1
      when(guess2 <= io.cmd.value) {
        acc := guess
        acc2 := guess2
      }
    } otherwise {
      io.rsp.valid := True
      when(io.rsp.ready) {
        clear()
      }
    }
  }

  val ini = RegInit(True)

  when(ini) {
    ini := False
    clear()
  }
}
