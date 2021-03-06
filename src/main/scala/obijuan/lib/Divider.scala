package obijuan.lib

import spinal.core._
import spinal.lib._

case class Divider(M: Int = 104) extends Component {
  val io = new Bundle {
    val tick = out Bool
  }

  val N = log2Up(M)

  val cnt = RegInit(U(0, N bits))
  io.tick := cnt(N - 1)

  cnt := (cnt === M - 1) ? U(0) | cnt + 1
}
