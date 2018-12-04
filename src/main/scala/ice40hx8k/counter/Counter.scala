package ice40hx8k.counter

import spinal.core._
import spinal.lib._

//noinspection TypeAnnotation
case class Counter(width: Int = 30, nleds: Int = 8) extends Component {
  val io = new Bundle {
    val leds = out UInt (nleds bits)
  }

  private val cnt = RegInit(U(0, width bits))
  io.leds := cnt(width - 1 downto width - nleds)

  cnt := cnt + 1
}
