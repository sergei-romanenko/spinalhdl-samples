package shs.sqrt

// This example is based on
// http://iverilog.wikia.com/wiki/Simulation

import spinal.core._
import spinal.lib._

case class SqrtI2(resultWidth: Int) extends Component {
  val valueWidth = 2 * resultWidth

  def valueType = UInt(valueWidth bits)
  def resultType = UInt(resultWidth bits)

  val io = new Bundle {
    val cmd = slave Stream valueType
    val rsp = master Stream resultType
  }

  // Keep track of which bit I'm working on.
  val lw = log2Up(resultWidth)
  val l = UInt(lw bits)
  val i = Reg(UInt(lw + 1 bits))
  l := (i - 1) (0 until lw)

  val b = resultType
  b := U(1) << l
  val b2 = valueType
  b2 := U(1) << (l << 1)

  // `acc` holds the accumulated result, and `acc2` is
  //  the accumulated square of the accumulated result.
  val acc = Reg(resultType)
  val acc2 = Reg(valueType)

  // guess holds the potential next values for acc,
  // and guess2 holds the square of that guess.
  val guess = resultType
  guess := acc | b
  val guess2 = valueType
  guess2 := acc2 + b2 + ((acc << l) << 1)

  def clear(): Unit = {
    io.cmd.ready := True
    acc := 0
    acc2 := 0
    i := U(resultWidth)
  }

  //Apply default assignement
  io.rsp.payload := acc
  io.cmd.ready := False
  io.rsp.valid := False

  when(io.cmd.valid) {
    //Is the sqrt iteration done?
    when(i =/= 0) {
      i := i - 1
      when(guess2 <= io.cmd.payload) {
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
