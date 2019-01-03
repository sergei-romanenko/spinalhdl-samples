package shs.ram

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

case class SumRAM(width: BigInt = 16, size: Int = 1024) extends Component {

  def wordType = UInt(width bits)

  def addressType = UInt(log2Up(size) bits)

  val io = new Bundle {
    val valid = out Bool
    val result = out(wordType)
  }

  val w_addr = Reg(addressType)
  val r_addr = Reg(addressType)

  val ram = new Mem(wordType, wordCount = size)

  val sum = Reg(wordType)
  io.valid := False
  io.result := 0

  val data = ram.readSync(address = r_addr)

  val fsm = new StateMachine {
    val INIT = new State with EntryPoint
    val START, LOOP, FINISH, DONE = new State

    INIT.onEntry(w_addr := 0)
    INIT.whenIsActive {
      ram.write(address = w_addr, data = w_addr + 1)
      when(w_addr === size - 1) {
        r_addr := 0
        goto(START)
      } otherwise {
        w_addr := w_addr + 1
      }
    }

    START.whenIsActive {
      r_addr := 1
      sum := 0
      goto(LOOP)
    }

    LOOP.whenIsActive {
      sum := sum + data
      when(r_addr === size - 1) {
        goto(FINISH)
      } otherwise {
        r_addr := r_addr + 1
      }
    }

    FINISH.whenIsActive {
      sum := sum + data
      goto(DONE)
    }

    DONE.whenIsActive {
      io.result := sum
      io.valid := True
    }
  }
}
