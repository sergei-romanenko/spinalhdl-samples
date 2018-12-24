package obijuan.t22syncrules

import spinal.core._
import spinal.lib._

// The module that sends a character when dtr is 1.
// The output tx is in a register.

case class TxTest1
(
  BAUD: Int = BaudGen.B300,
  CHAR: Char = 'K'
) extends Component {
  val io = new Bundle {
    val dtr = in Bool
    val tx = out Bool
  }

  val charByte: Bits = B(CHAR.toByte, 8 bits)

  val tx = RegInit(True)
  io.tx := tx

  val enable = RegInit(False)
  enable := io.dtr

  val bg = BaudGen(BAUD)
  bg.io.enable := enable
  val tick = Bool
  tick := bg.io.tick

  val shifter = Reg(Bits(width = 10 bits))
  tx := enable ? shifter(0) | True

  val byteK = 'K'.toByte

  when(!enable) {
    shifter := charByte ## B"2'b01"
  } elsewhen (enable && tick) {
    shifter := U"1'b1" ## shifter(9 downto 1)
  }
}
