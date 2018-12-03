package ice40hx8k.counter

import spinal.core._
import spinal.lib._

//noinspection TypeAnnotation
case class Counter(WIDTH: Int = 30, NLEDS: Int = 8) extends Component {
  val io = new Bundle {
    val leds = out UInt (NLEDS bits)
  }

  private val cnt = RegInit(U(0, WIDTH bits))
  io.leds := cnt(WIDTH - 1 downto WIDTH - NLEDS)

  cnt := cnt + 1
}
