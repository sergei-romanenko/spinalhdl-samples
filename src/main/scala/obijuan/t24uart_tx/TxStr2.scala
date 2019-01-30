package obijuan.t24uart_tx

import spinal.core._
import spinal.lib._
import obijuan.lib._

// When DTR is on, continuously sending the string "Hello!.."

case class TxStr2
(
  BAUDRATE: Int = BaudGen.B300,
  DELAY: Int = Freq.T_1s
) extends Component {
  val io = new Bundle {
    val tx = out Bool
  }

  val div = DividerP1(DELAY)

  val data = Reg(Bits(width = 8 bits))
  val tick = RegNext(div.io.tick) init False

  val utx = UartTx(BAUDRATE)

  utx.io.data := data
  utx.io.start := False

  io.tx := utx.io.tx

  // === Data routing

  val char_no = Reg(UInt(width = 3 bits)) init 0

  val msg = "Hello!..".map(c => B(c.toByte, 8 bits)).toVector

  switch(char_no) {
    for (i <- msg.indices) {
      is(i)(data := msg(i))
    }
  }

  // === Controller

  // States
  object FsmState extends SpinalEnum {
    val IDLE, START, NEXT, END = newElement()
  }

  import FsmState._

  // === FSM

  val state = RegInit(IDLE)

  switch(state) {
    is(IDLE) {
      when(tick)(state := START)
    }

    is(START) {
      utx.io.start := True
      when(utx.io.ready)(state := NEXT)
    }

    is(NEXT) {
      char_no := char_no + 1
      when(char_no === 7)(state := END) otherwise (state := START)
    }

    is(END) {
      when(utx.io.ready)(state := IDLE)
    }
  }

}
