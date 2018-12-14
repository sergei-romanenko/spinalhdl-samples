package obijuan.t26rom

import spinal.core._

import obijuan.lib._

case class RomFile4x4(romfile: String = "rom1.list") extends Component {
  val io = new Bundle {
    val addr = in UInt (4 bits)
    val data = out UInt (4 bits)
  }

  val elements_from_file : Seq[BigInt] =
    Tools.readmemh(romfile)

  var rom = Vec(
    elements =
      for (e <- elements_from_file)
        yield U(e, 4 bits),
    UInt(width = 4 bits))
  io.data := rom(io.addr)
}
