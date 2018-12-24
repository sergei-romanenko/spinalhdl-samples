package obijuan.t26rom

import obijuan.lib._
import spinal.core._

case class RomFile4x4Tester
(
  DELAY: Long = Freq.T_500ms,
  ROMFILE: String = "rom1.list"
) extends Component {

  val io = new Bundle {
    val leds = out UInt (8 bits)
  }

  val div = DividerP1(M = DELAY)
  val tick = Bool
  tick := div.io.tick

  val start = RegInit(True)
  start := False

  val rom = RomFile4x4(ROMFILE)
  val addr = RegInit(U(0, 4 bits))
  rom.io.addr := addr

  io.leds(3 downto 0) := start ? U(0, 4 bits) | rom.io.data
  io.leds(7 downto 4) := U(0, 4 bits)

  when(!start & tick) {
    addr := addr + 1
  }
}
