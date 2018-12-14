package obijuan.t26rom

import obijuan.lib._
import spinal.core._

case class MemRom4x4(romfile: String = "rom1.list") extends Component {
  val io = new Bundle {
    val addr = in UInt (4 bits)
    val data = out UInt (4 bits)
  }

  val rom = new Mem(wordType = UInt(width = 4 bits), wordCount = 16)
  rom.initialContent = Tools.readmemh(romfile)
  io.data := rom.readSync(address = io.addr)
}
