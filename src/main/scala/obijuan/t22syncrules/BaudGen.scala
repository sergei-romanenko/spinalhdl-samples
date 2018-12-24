package obijuan.t22syncrules

import spinal.core._
import spinal.lib._

object BaudGen {
  val B115200 = 104
  val B57600 = 208
  val B38400 = 313
  val B19200 = 625
  val B9600 = 1250
  val B4800 = 2500
  val B2400 = 5000
  val B1200 = 10000
  val B600 = 20000
  val B300 = 40000
}

case class BaudGen(M: Int = BaudGen.B115200) extends Component {
  val io = new Bundle {
    val enable = in Bool
    val tick = out Bool
  }

  val N = log2Up(M)
  val cnt = RegInit(U(0, N bits))

  io.tick := (cnt === 0) ? io.enable | False

  when(io.enable) {
    cnt := (cnt === M - 1) ? U(0) | cnt + 1
  } otherwise {
    cnt := M - 1
  }
}
