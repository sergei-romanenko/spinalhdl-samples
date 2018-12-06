package obijuan.t26rom

import spinal.core._

//noinspection TypeAnnotation,FieldFromDelayedInit
case class Rom4x4Tester(DELAY: Long = Freq.T_500ms) extends Component {
  val io = new Bundle {
    val leds = out UInt (8 bits)
  }

  val addr = RegInit(U(0, 4 bits))
  val rom = Rom4x4()

  rom.io.addr := addr
  io.leds(3 downto 0) := rom.io.data
  io.leds(7 downto 4) := U(0, 4 bits)

  val div = Divider(M = DELAY)
  val tick = Bool
  tick := div.io.tick

  val start = RegInit(Bool(value = true))
  start := False

  when(tick) {
    addr := addr + 1
  }
  when(start) {
    addr := 0
  }
}
