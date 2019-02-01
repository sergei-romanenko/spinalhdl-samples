package shs.uart

import spinal.core._
import spinal.lib._

import shs.lib._

case class TxChar(BAUDRATE: Int = BaudGen.B115200) extends Component {
  val io = new Bundle {
    val tx = out Bool
  }

  val utx = UartTx(BAUDRATE)
  utx.io.cmd.payload := B('A'.toByte, 8 bits)
  utx.io.cmd.valid := True
  io.tx := utx.io.tx
}
