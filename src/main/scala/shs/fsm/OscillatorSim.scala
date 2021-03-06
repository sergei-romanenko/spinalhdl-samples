package shs.fsm

import spinal.core._
import spinal.sim._
import spinal.core.sim._

object OscillatorSim {
  val spinalConfig = SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
    defaultClockDomainFrequency = FixedFrequency(10 Hz))

  def main(args: Array[String]) {
    val compiled = SimConfig.withConfig(spinalConfig)
      .withWave.compile(Oscillator(N = 4, frequency = 2 Hz))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      for (k <- 0 until 100) {
        dut.clockDomain.waitSampling()
      }
    }
  }
}
