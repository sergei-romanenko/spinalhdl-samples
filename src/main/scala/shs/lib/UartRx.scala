package shs.lib

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

case class UartRx(BAUDRATE: Int) extends Component {
  val io = new Bundle {
    val rx = in Bool
    val rsp = master Flow Bits (8 bits)
  }

  val valid = Reg(Bool) init False
  io.rsp.valid := valid
  val data = Reg(Bits(width = 8 bits)) init 0
  io.rsp.payload := data

  // === Microinstructions

  val baud_enable = Reg(Bool) init False
  val clear = Reg(Bool) init False
  val load = Reg(Bool) init False

  // == Baudrate ticks

  val baud_tick = Bool
  val bg = BaudGenRx(BAUDRATE)
  bg.io.enable := baud_enable
  baud_tick := bg.io.tick

  // === Data routing

  val rx = RegNext(io.rx)

  val bitc = Reg(UInt(width = 4 bits))

  when(clear) {
    bitc := 0
  } elsewhen (baud_tick) {
    bitc := bitc + 1
  }

  val raw_data = Reg(Bits(width = 10 bits))

  when(baud_tick) {
    raw_data := rx ## raw_data(9 downto 1)
  }

  when(load) {
    data := raw_data(8 downto 1)
  }

  // === Controller

  // Defaults
  baud_enable := False
  clear := False
  load := False
  valid := False

  val fsm = new StateMachine {
    val IDLE: State = new State with EntryPoint
    val RECV, LOAD, DAV: State = new State

    IDLE.whenIsActive {
      clear := True
      when(!rx) {
        goto(RECV)
      }
    }

    RECV.whenIsActive {
      baud_enable := True
      when(bitc === 10) {
        goto(LOAD)
      }
    }

    LOAD.whenIsActive {
      load := True
      goto(DAV)
    }

    DAV.whenIsActive {
      valid := True
      when(rx) {
        goto(IDLE)
      }
    }
  }

}
