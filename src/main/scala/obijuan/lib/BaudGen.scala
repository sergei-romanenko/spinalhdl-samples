package obijuan.lib

import spinal.core._

object BaudGen {

  val B115200: Int = 104
  val B57600: Int = 208
  val B38400: Int = 313

  val B19200: Int = 625
  val B9600: Int = 1250
  val B4800: Int = 2500
  val B2400: Int = 5000
  val B1200: Int = 10000
  val B600: Int = 20000
  val B300: Int = 40000

}

case class BaudGen(M: Int = BaudGen.B115200) extends Component {
  val io = new Bundle {
    val enable = in Bool
    val tick = out Bool
  }

  val max = M - 1
  val width = log2Up(M)

  val cnt = RegInit(U(0, width bits))

  when(io.enable) {
    cnt := (cnt === max) ? U(0) | cnt + 1
  } otherwise {
    cnt := max
  }

  io.tick := (cnt === U(0)) ? io.enable | False
}
