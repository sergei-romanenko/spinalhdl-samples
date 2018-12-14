package shs.ram

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

case class Sum(width: Int = 16, size: Int = 1024) extends Component {
  val io = new Bundle {
    val ready = out Bool
    val result = out UInt (width bits)
  }

  val w = log2Up(size)
  val w_addr = RegInit(U(0, w bits))
  val r_addr = RegInit(U(0, w bits))

  val ram = new Mem(
    wordType = UInt(width = width bits), wordCount = size)

  val sum = RegInit(U(0, width bits))
  io.ready := False
  io.result := 0

  val w1 = log2Up(size + 1)
  val cnt = RegInit(U(0, w1 bits))
  val data = ram.readSync(address = r_addr)

  val fsm = new StateMachine {

    val INIT: State = new State with EntryPoint {
      whenIsActive {
        ram.write(address = w_addr, data = w_addr)
        when(w_addr === size - 1) {
          goto(LOOP)
        } otherwise {
          w_addr := w_addr + 1
        }
      }
    }

    val LOOP: State = new State {
      whenIsActive {
        sum := sum + ram.readSync(address = r_addr)
        when(cnt === size) {
          goto(DONE)
        } otherwise {
          cnt := cnt + 1
          r_addr := r_addr + 1
        }
      }
    }

    val DONE: State = new State {
      whenIsActive {
        io.result := sum
        io.ready := True
      }
    }
  }
}
