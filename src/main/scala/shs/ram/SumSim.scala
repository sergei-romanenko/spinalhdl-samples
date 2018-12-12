package shs.ram

import spinal.core._
import spinal.sim._
import spinal.core.sim._

//noinspection FieldFromDelayedInit,LanguageFeature
object SumSim {
  val spinalConfig = SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
    defaultClockDomainFrequency = FixedFrequency(10 Hz))

  def main(args: Array[String]) {
    val compiled = SimConfig.withConfig(spinalConfig)
      .withWave.compile(Sum(width = 8, size = 5))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      Suspendable.repeat (100) {
        dut.clockDomain.waitSampling()
      }
    }
  }
}
