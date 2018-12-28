package shs.min

import spinal.core._
import spinal.lib._

case class Min(w: Int, n: Int) extends Component {
  def valueType = UInt(w bits)

  val io = new Bundle {
    val cmd = slave Flow Vec(valueType, n)
    val rsp = master Flow valueType
  }
  val us = io.cmd.payload

  def build(k: Int, l: Int, r: UInt): Unit = {
    assert(isPow2(l), message = "Input width !isPow2")
    assert(l != 0, message = "Input width == 0")
    if (l == 1) {
      r := us(k)
    } else {
      val l1 = l / 2
      val l2 = l - l1
      val r1, r2 = Reg(valueType) init 0
      build(k, l1, r1)
      build(k + l1, l2, r2)
      when(r1 <= r2) {
        r := r1
      } otherwise {
        r := r2
      }
    }
  }

  build(0, us.length, io.rsp.payload)
  val latency = LatencyAnalysis(us.head, io.rsp.payload)
  println(s"latency = $latency")
  io.rsp.valid := Delay(io.cmd.valid, cycleCount = latency, init = False)
}
