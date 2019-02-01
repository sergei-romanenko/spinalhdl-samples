package obijuan.lib

import spinal.core._
import spinal.lib._

case class UartTx(BAUDRATE: Int) extends Component {
  val io = new Bundle {
    val start = in Bool
    val data = in Bits (8 bits)
    val tx = out Bool
    val ready = out Bool
  }

  val start = RegNext(io.start) init False
  val data = RegNext(io.data)
  val tx = Reg(Bool)
  io.tx := tx

  // === Microinstructions

  val load = Bool
  val baud_enable = Bool

  // == Baudrate ticks

  val baud_tick = Bool

  val bg = new BaudGen(BAUDRATE)
  bg.io.enable := baud_enable
  baud_tick := bg.io.tick

  // === Data routing

  val shifter = RegInit(B"10'b11_1111_1111")

  when(load) {
    shifter := data ## B"2'b01"
  } elsewhen (baud_tick) {
    shifter := B"1'b1" ## shifter(9 downto 1)
  }

  val bitc = Reg(UInt(width = 4 bits))

  when(load) {
    bitc := 0
  } elsewhen (baud_tick) {
    bitc := bitc + 1
  }

  // Send a bit to tx.
  // Since `shifter` is a register, there will be no spurious signals.
  tx := shifter(0)

  // === Controller

  // States
  object FsmState extends SpinalEnum {
    val IDLE, START, TRANS = newElement()
  }

  import FsmState._

  // === FSM

  val state = RegInit(IDLE)

  switch(state) {
    is(IDLE) {
      when(start)(state := START) otherwise (state := IDLE)
    }

    is(START) {
      state := TRANS
    }

    is(TRANS) {
      when(bitc === 11)(state := IDLE) otherwise (state := TRANS)
    }
  }

  // === Generating the microinstructions

  load := (state === START)
  baud_enable := (state =/= IDLE)

  io.ready := (state === IDLE)

  when(io.start && state === IDLE) {
    data := io.data
  }
}
