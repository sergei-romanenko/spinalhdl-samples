package obijuan.t26rom

import spinal.core._
import spinal.lib._

//noinspection TypeAnnotation
case class Rom5x4() extends Component {
  val io = new Bundle {
    val addr = in UInt (5 bits)
    val data = out UInt (4 bits)
  }

  var rom = Vec(
    elements =
      for (i <- 0 until 32)
        yield U(if (i < 8) i else 0, 4 bits),
    UInt(width = 4 bits))
  io.data := rom(io.addr)
}
