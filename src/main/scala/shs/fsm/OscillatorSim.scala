package shs.fsm

import spinal.core._
import spinal.sim._
import spinal.core.sim._

//noinspection FieldFromDelayedInit,LanguageFeature
object OscillatorSim {
  def main(args: Array[String]) {
    val compiled = SimConfig.withWave.compile(Oscillator(N = 4, factor = 5))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      Suspendable.repeat (100) {
        dut.clockDomain.waitSampling()
      }
    }
  }
}
