package shs.uart

import spinal.core._
import spinal.lib._

import shs.lib._

case class RxEcho(BAUDRATE: Int = BaudGen.B115200) extends Component {
  val io = new Bundle {
    val rx = in Bool
    val tx = out Bool
    val leds = out Bits(width = 8 bits)
  }

  val leds = Reg(Bits(width = 8 bits))
  io.leds := leds

  val valid = Bool
  val data = Bits(width = 8 bit)
  val ready = Bool

  val urx = UartRx(BAUDRATE)
  urx.io.rx := io.rx
  valid := urx.io.rsp.valid
  data := urx.io.rsp.payload

  val utx = UartTx(BAUDRATE)
  utx.io.cmd.valid := valid
  utx.io.cmd.payload := data
  io.tx := utx.io.tx
  ready := utx.io.cmd.ready

  leds := data
}
