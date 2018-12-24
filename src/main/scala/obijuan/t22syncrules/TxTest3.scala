package obijuan.t22syncrules

import spinal.core._
import obijuan.lib._

// The module that continuously sends a character.
// The output tx is in a register.

case class TxTest3
(
  BAUD: Int = BaudGen.B300,
  DELAY: Int = Freq.T_250ms,
  CHAR: Char = 'K'
) extends Component {
  val io = new Bundle {
    val tx = out Bool
  }

  val charByte: Bits = B(CHAR.toByte, 8 bits)

  val tx = RegInit(True)
  io.tx := tx

  val dv = Divider(DELAY)
  val enable = RegInit(False)
  enable := dv.io.tick

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
    shifter := B("1'b1") ## shifter(9 downto 1)
  }
}
