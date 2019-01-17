package shs.btsort

import spinal.core._
import spinal.lib._

case class BitonicSort(width: Int = 8, size: Int = 16) extends Component {
  require((size & (size - 1)) == 0, "Size must be a power of 2")

  def dataType = UInt(width bits)

  val io = new Bundle {
    val cmd = slave Flow Vec(dataType, size)
    val rsp = master Flow Vec(dataType, size)
  }

  def swap(up: Boolean, d1: UInt, d2: UInt, q1: UInt, q2: UInt): Unit = {
    val cmp = if (up) d1 <= d2 else d1 >= d2
    q1 := cmp ? d1 | d2
    q2 := cmp ? d2 | d1
  }

  def merge(up: Boolean, d: Vector[UInt], q: Vector[UInt]): Unit = {
    val l = d.length
    val h = l / 2

    if (l == 1) {
      q(0) := d(0)
      return
    }

    val t = Vector.fill(l)(dataType)

    for (i <- 0 until h)
      swap(up, d(i), d(i + h), t(i), t(i + h))

    merge(up, t.take(h), q.take(h))
    merge(up, t.drop(h), q.drop(h))
  }

  def sort(up: Boolean, d: Vector[UInt], q: Vector[UInt]): Unit = {
    val l = d.length

    if (l == 1) {
      q(0) := d(0)
      return
    }

    val h = l / 2
    val t = Vector.fill(l)(dataType)

    sort(up = true, d.take(h), t.take(h))
    sort(up = false, d.drop(h), t.drop(h))
    merge(up, t, q)
  }

  io.rsp.valid := io.cmd.valid
  sort(up = true, d = io.cmd.payload.toVector, q = io.rsp.payload.toVector)
}
