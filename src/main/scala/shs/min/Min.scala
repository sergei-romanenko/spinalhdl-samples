package shs.min

import spinal.core._
import spinal.lib._

case class Min(w: Int, n: Int) extends Component {
  def valueType = UInt(w bits)

  val io = new Bundle {
    val cmd = slave Flow Vec(valueType, n)
    val rsp = master Flow valueType
  }

  def build(d: Int, es: Vector[UInt]): UInt = {
    if (d == 0) {
      val r0 = Reg(valueType) init 0
      r0 := es.head
      r0
    } else if (es.length == 1) {
      val r0 = Reg(valueType) init 0
      r0 := build(d - 1, es)
      r0
    } else {
      val l = es.length / 2
      val r1 = build(d - 1, es.take(l))
      val r2 = build(d - 1, es.drop(l))
      val r = Reg(valueType) init 0
      when(r1 <= r2) {
        r := r1
      } otherwise {
        r := r2
      }
      r
    }
  }

  require(n > 0, "Input width == 0")
  val depth = log2Up(n)

  io.rsp.payload := build(depth, io.cmd.payload.toVector)

  val latency = LatencyAnalysis(io.cmd.payload.head, io.rsp.payload)
  println(s"latency = $latency")
  io.rsp.valid := Delay(io.cmd.valid, cycleCount = latency, init = False)
}
