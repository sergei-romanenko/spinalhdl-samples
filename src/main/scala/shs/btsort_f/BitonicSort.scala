package shs.btsort_f

import spinal.core._
import spinal.lib._

case class BitonicSort(width: Int = 8, size: Int = 16) extends Component {
  require((size & (size - 1)) == 0, "Size must be a power of 2")

  def dataType = UInt(width bits)

  val io = new Bundle {
    val values = in Vec(dataType, size)
    val results = out Vec(dataType, size)
  }

  def merge(up: Boolean, d: Vector[UInt]): Vector[UInt] = {
    val l = d.length
    if (l == 1) {
      return d
    }

    val h = l / 2
    val (d1, d2) = d.splitAt(h)

    val ok: Vector[Bool] = Vector.tabulate(h)(
      i => if (up) d1(i) <= d2(i) else d2(i) <= d1(i))

    val q1 = Vector.tabulate(h)(i => ok(i) ? d1(i) | d2(i))
    val q2 = Vector.tabulate(h)(i => ok(i) ? d2(i) | d1(i))

    merge(up, q1) ++ merge(up, q2)
  }

  def sort(up: Boolean, d: Vector[UInt]): Vector[UInt] = {
    val l = d.length

    if (l == 1) {
      return d
    }

    val h = l / 2
    val (d1, d2) = d.splitAt(h)

    val t1 = sort(up = true, d1)
    val t2 = sort(up=false, d2)
    merge(up, t1 ++ t2)
  }

  io.results := Vec(sort(up = true, io.values.toVector))
}
