package shs.lib

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

// Originally written in Verilog by Juan González-Gómez (Obijuan)
// and Jesús Arroyo Torrens: <https://github.com/FPGAwars/FPGA-peripherals>

case class UartTx(BAUDRATE: Int = BaudGen.B115200) extends Component {
  val io = new Bundle {
    val cmd = slave Stream Bits (width = 8 bits)
    val tx = out Bool // Serial payload output
  }

  // DATAPATH

  val data = Reg(Bits(width = 8 bits))
  //val ready = RegInit(False)
  //io.cmd.ready := ready

  // === Microinstructions

  val load = Bool   // Load the shifter register / reset
  val baud_enable = Bool

  // == Baudrate ticks

  val baud_tick = Bool

  val bg = BaudGen(BAUDRATE)
  bg.io.enable := baud_enable
  baud_tick := bg.io.tick

  // === Data routing

  // `shifter` stores the frame to transmit:
  // 1 valid bit + 8 payload bits + 1 stop bit
  val shifter = RegInit(B"10'b11_1111_1111")

  // Sent bit counter
  val bitc = Reg(UInt(width = 4 bits)) init 0

  when(load) {
    shifter := data ## B"2'b01"
    bitc := 0
  } elsewhen (baud_tick) {
    shifter := B"1'b1" ## shifter(9 downto 1)
    bitc := bitc + 1
  }

  // Send a bit to tx.
  // Since `shifter` is a register, there will be no spurious signals.
  io.tx := shifter(0)

  // === Controller

  // Defaults
  load := False
  baud_enable := True
  io.cmd.ready := False

  // Control signal generation

  val fsm = new StateMachine {
    val IDLE: State = new State with EntryPoint
    val START, TRANS: State = new State

      IDLE.whenIsActive {
        io.cmd.ready := True
        baud_enable := False
        when(io.cmd.valid) {
          data := io.cmd.payload
          goto(START)
        }
      }

      START.whenIsActive {
        load := True
        goto(TRANS)
      }

      TRANS.whenIsActive {
        when(bitc === 11) {
          goto(IDLE)
        }
      }
    }
}
