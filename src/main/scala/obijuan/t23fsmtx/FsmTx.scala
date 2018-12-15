package obijuan.t23fsmtx

import spinal.core._
import spinal.lib._

case class FsmTx
(
  BAUD: Int = BaudGen.B300,
  CHAR: Char = 'A'
) extends Component {
  val io = new Bundle {
    val dtr = in Bool
    val tx = out Bool
  }

  val byte: Bits = B(CHAR.toByte, 8 bits)

  val start = RegInit(False)
  start := io.dtr

  // === Initialization

  val ini = RegInit(True)
  ini := False

  // === Microinstructions
  val load = Bool
  val baud_enable = Bool

  // Slow ticks

  val baud_tick = Bool
  val boud0 = BaudGen(BAUD)
  boud0.io.enable := baud_enable
  baud_tick := boud0.io.tick

  // === Data routing

  val shifter = Reg(Bits(width = 10 bits))

  when(ini) {
    shifter := B"b11_1111_1111"
  } elsewhen load {
    shifter := B(CHAR.toByte, 8 bits) ## B"2'b01"
  } elsewhen baud_tick {
    shifter := U"1'b1" ## shifter(9 downto 1)
  }

  val bitc = Reg(UInt(width = 4 bits))

  when(load) {
    bitc := 0
  } elsewhen baud_tick {
    bitc := bitc + 1
  }

  // Send a bit to tx.
  // Since `shifter` is a register, there will be no spurious signals.
  io.tx := shifter(0)

  // === Controller

  // States
  object FsmState extends SpinalEnum {
    val IDLE, START, TRANS = newElement()
  }

  import FsmState._

  val state = RegInit(IDLE)

  when(ini) {
    state := IDLE
  } otherwise {
    switch(state) {

      is(IDLE) {
        when(start) {
          state := START
        } otherwise {
          state := IDLE
        }
      }

      is(START) {
        state := TRANS
      }

      is(TRANS) {
        when(bitc === 11) {
          state := IDLE
        } otherwise {
          state := TRANS
        }
      }
    }
  }

  // === Generating the microinstructions
  load := (state === START)
  baud_enable := (state =/= IDLE)
}
