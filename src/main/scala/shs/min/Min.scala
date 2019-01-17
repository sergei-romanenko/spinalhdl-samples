package shs.min

import spinal.core._
import spinal.lib._

case class Min(w: Int, n: Int) extends Component {
  require(n > 0, "Input width can't be == 0")

  def valueType = UInt(w bits)

  val io = new Bundle {
    val cmd = slave Flow Vec(valueType, n)
    val rsp = master Flow valueType
  }

  def build(d: Int, es: Vector[UInt]): UInt = {
    if (d == 0) {
      es.head
    } else if (es.length == 1) {
      build(d - 1, es)
    } else {
      val l = es.length / 2
      val r1 = build(d - 1, es.take(l))
      val r2 = build(d - 1, es.drop(l))
      (r1 <= r2) ? r1 | r2
    }
  }

  val depth = log2Up(n)

  io.rsp.payload := build(depth, io.cmd.payload.toVector)

  val latency = LatencyAnalysis(io.cmd.payload.head, io.rsp.payload)
  println(s"latency = $latency")
  io.rsp.valid := Delay(io.cmd.valid, cycleCount = latency, init = False)
}
