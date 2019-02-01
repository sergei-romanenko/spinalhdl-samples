package obijuan.lib

import spinal.core._
import spinal.lib._

// The positive pulse width is 1 clock cycle.

case class BaudGenRx(BAUDRATE: Int) extends Component {
  val io = new Bundle {
    val enable = in Bool
    val tick = out Bool
  }

  val W = log2Up(BAUDRATE)
  val max = BAUDRATE - 1
  // Value to generate pulse in the middle of the period
  val half = BAUDRATE >> 1

  val cnt = Reg(UInt(W bits)) init 0;

  when(io.enable) {
    cnt := (cnt === max) ? U(0) | (cnt + 1)
  } otherwise {
    cnt := max
  }

  io.tick := io.enable && (cnt === half)
}
