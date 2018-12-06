package obijuan.t26rom

import spinal.core._

//noinspection TypeAnnotation
case class Rom4x4() extends Component {
  val io = new Bundle {
    val addr = in UInt (4 bits)
    val data = out UInt (4 bits)
  }

  val elements = Seq(
    0x1, 0x2, 0x4, 0x8,
    0x1, 0x8, 0x4, 0x2,
    0x1, 0xF, 0x0, 0xF,
    0xC, 0x3, 0xC, 0x3)

  var rom = Vec(
    elements =
      for (e <- elements)
        yield U(e, 4 bits),
    UInt(width = 4 bits))
  io.data := rom(io.addr)
}
