package shs.uart

import spinal.core._
import spinal.lib._

import shs.lib._

// The received characters are stored in a register and the least
// significant bits are displayed by the LEDs.

case class RxLeds(BAUDRATE: Int = BaudGen.B115200) extends Component {
  val io = new Bundle {
    val rx = in Bool
    val leds = out Bits(8 bits)
  }

  val leds = Reg(Bits(width = 8 bits)) init 0
  io.leds := leds

  val valid = Bool
  val data = Bits(width = 8 bits)

  val urx = UartRx(BAUDRATE)
  urx.io.rx := io.rx
  valid := urx.io.rsp.valid
  data := urx.io.rsp.payload

  when(valid) {
    leds := data(7 downto 0)
  }
}
