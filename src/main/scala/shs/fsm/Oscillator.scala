package shs.fsm

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

//noinspection TypeAnnotation,LanguageFeature,ForwardReference
case class Oscillator(N: Int, frequency: HertzNumber) extends Component {
  val width = log2Up(N)
  val io = new Bundle {
    val ampl = out UInt (width bits)
  }

  val slowArea = new SlowArea(frequency) {

    val fsm = new StateMachine {

      val counter = RegInit(U(0, width bits))

      val UP: State = new State with EntryPoint {
        whenIsActive {
          counter := counter + 1
          when(counter === N - 1) {
            counter := counter - 1
            goto(DOWN)
          }
        }
      }

      val DOWN: State = new State {
        whenIsActive {
          counter := counter - 1
          when(counter === 0) {
            counter := counter + 1
            goto(UP)
          }
        }
      }
    }

    io.ampl := fsm.counter
  }
}
