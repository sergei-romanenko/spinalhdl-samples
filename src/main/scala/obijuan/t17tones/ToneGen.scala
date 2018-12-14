package obijuan.t17tones

import spinal.core._
import spinal.lib._
import obijuan.lib.Freq

case class ToneGen(M: Long = Freq.F_2KHz) extends Component {
  val io = new Bundle {
    val tick = out Bool
  }

  val N = log2Up(M)
  val max = U(M - 1, N bits)

  val cnt = RegInit(U(0, N bits))
  io.tick := cnt(N - 1)
  cnt := (cnt === max) ? U(0) | (cnt + 1)
}
