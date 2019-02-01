package obijuan.lib

import spinal.core._
import spinal.lib._

case class UartRx(BAUDRATE: Int) extends Component {
  val io = new Bundle {
    val rx = in Bool
    val rcv = out Bool
    val data = out Bits (8 bits)
  }

  val rcv = Reg(Bool)
  io.rcv := rcv
  val data = Reg(Bits(width = 8 bits)) init 0
  io.data := data

  // === Microinstructions

  val baud_enable = Reg(Bool)
  val clear = Reg(Bool)
  val load = Reg(Bool)

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

  // States
  object FsmState extends SpinalEnum {
    val IDLE, RECV, LOAD, DAV = newElement()
  }

  import FsmState._

  // === FSM

  val state = RegInit(IDLE)

  switch(state) {
    is(IDLE) {
      when(!rx)(state := RECV) otherwise (state := IDLE)
    }

    is(RECV) {
      when(bitc === 10)(state := LOAD) otherwise (state := RECV)
    }

    is(LOAD) {
      state := DAV
    }

    is(DAV) {
      when(rx) (state := IDLE) otherwise(DAV)
    }
  }

  // === Generating the microinstructions

  baud_enable := (state === RECV)
  clear := (state === IDLE)
  load := (state === LOAD)
  rcv := (state === DAV)
}
