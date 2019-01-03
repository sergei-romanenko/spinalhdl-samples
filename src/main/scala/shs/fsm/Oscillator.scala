package shs.fsm

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

case class Oscillator(N: Int, frequency: HertzNumber) extends Component {
  val width = log2Up(N)
  val io = new Bundle {
    val ampl = out UInt (width bits)
  }

  val slowArea = new SlowArea(frequency) {

    val fsm = new StateMachine {
      val FORTH: State = new State with EntryPoint
      val BACK: State = new State

      val counter = RegInit(U(0, width bits))

      FORTH.whenIsActive {
        counter := counter + 1
        when(counter === N - 1) {
          counter := counter - 1
          goto(BACK)
        }
      }

      BACK.whenIsActive {
        counter := counter - 1
        when(counter === 0) {
          counter := counter + 1
          goto(FORTH)
        }
      }
    }

    io.ampl := fsm.counter
  }
}
