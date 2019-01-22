package shs.min

import spinal.core._
import spinal.lib._

case class Min(w: Int, n: Int) extends Component {
  require(n > 0, "Input width can't be == 0")

  def valueType = UInt(w bits)

  val io = new Bundle {
    val values = in(Vec(valueType, n))
    val result = out(valueType)
  }

  def build(es: Vector[UInt]): UInt = {
    if (es.length == 1) {
      es.head
    } else {
      val l = es.length / 2
      val r1 = build(es.take(l))
      val r2 = build(es.drop(l))
      (r1 <= r2) ? r1 | r2
    }
  }

  io.result := build(io.values.toVector)
}
