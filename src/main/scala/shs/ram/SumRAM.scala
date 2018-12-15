package shs.ram

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

case class SumRAM(width: Int = 16, size: Int = 1024) extends Component {
  val io = new Bundle {
    val ready = out Bool
    val result = out UInt (width bits)
  }

  val w = log2Up(size)
  val w_addr = Reg(UInt(w bits))
  val r_addr = Reg(UInt(w bits))

  val ram = new Mem(
    wordType = UInt(width = width bits), wordCount = size)

  val sum = Reg(UInt(width bits))
  io.ready := False
  io.result := 0

  val data = ram.readSync(address = r_addr)

  val fsm = new StateMachine {
    val INIT = new State with EntryPoint
    val START, LOOP, FINISH, DONE = new State

    INIT
      .onEntry(w_addr := 0)
      .whenIsActive {
        ram.write(address = w_addr, data = w_addr + 1)
        when(w_addr === size - 1) {
          r_addr := 0
          goto(START)
        } otherwise {
          w_addr := w_addr + 1
        }
      }

    START
      .whenIsActive {
        r_addr := 1
        sum := 0
        goto(LOOP)
      }

    LOOP
      .whenIsActive {
        sum := sum + data
        when(r_addr === size - 1) {
          goto(FINISH)
        } otherwise {
          r_addr := r_addr + 1
        }
      }

    FINISH
      .whenIsActive {
        sum := sum + data
        goto(DONE)
      }

    DONE
      .whenIsActive {
        io.result := sum
        io.ready := True
      }
  }
}
