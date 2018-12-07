package obijuan.t26rom

import spinal.core._
import spinal.lib._

//noinspection TypeAnnotation,FieldFromDelayedInit,LanguageFeature
case class Rom5x4Tester() extends Component {
  val io = new Bundle {
    val leds = out UInt(8 bits)
  }

  val ADDR = U(5, 5 bits)
  val rom = Rom5x4()

  rom.io.addr := ADDR
  io.leds(3 downto 0) := rom.io.data
  io.leds(7 downto 4) := U(0, 4 bits)
}
