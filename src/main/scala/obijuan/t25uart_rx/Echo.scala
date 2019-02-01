package obijuan.t25uart_rx

import spinal.core._
import spinal.lib._
import obijuan.lib._

case class Echo(BAUDRATE: Int = BaudGen.B115200) extends Component {
  val io = new Bundle {
    val rx = in Bool
    val tx = out Bool
    val leds = out Bits(width = 8 bits)
  }

  val leds = Reg(Bits(width = 8 bits))
  io.leds := leds

  val rcv = Bool
  val data = Bits(width = 8 bit)
  val ready = Bool

  val urx = UartRx(BAUDRATE)
  urx.io.rx := io.rx
  rcv := urx.io.rcv
  data := urx.io.data

  val utx = UartTx(BAUDRATE)
  utx.io.start := rcv
  utx.io.data := data
  io.tx := utx.io.tx
  ready := utx.io.ready

  leds := data
}
