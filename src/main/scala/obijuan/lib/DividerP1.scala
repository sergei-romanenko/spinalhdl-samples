package obijuan.lib

import spinal.core._
import spinal.lib._

case class DividerP1(M: Long = Freq.T_100ms) extends Component {
  val io = new Bundle {
    val tick = out Bool
  }

  val N = log2Up(M)
  val max = U(M - 1, N bits)

  val cnt = RegInit(U(0, N bits))
  io.tick := cnt === 0
  cnt := (cnt === max) ? U(0) | (cnt + 1)
}
