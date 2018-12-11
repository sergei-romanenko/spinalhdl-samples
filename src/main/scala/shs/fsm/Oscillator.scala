package shs.fsm

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

//noinspection TypeAnnotation,LanguageFeature,ForwardReference
case class Oscillator(N: Int, factor: BigInt) extends Component {
  val io = new Bundle {
    val width = log2Up(N)
    val ampl = out UInt (width bits)
  }

  val slowArea = new SlowArea(factor = factor) {

    val fsm = new StateMachine {

      val counter = RegInit(U(0, io.width bits))

      val UP: State = new State with EntryPoint {
        whenIsActive {
          when(counter === N - 1) {
            counter := counter - 1
            goto(DOWN)
          } otherwise {
            counter := counter + 1
          }
        }
      }

      val DOWN: State = new State {
        whenIsActive {
          when(counter === 0) {
            counter := counter + 1
            goto(UP)
          } otherwise {
            counter := counter - 1
          }
        }
      }
    }

    io.ampl := fsm.counter
  }
}
