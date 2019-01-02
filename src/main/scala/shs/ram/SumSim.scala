package shs.ram

import spinal.core._
import spinal.sim._
import spinal.core.sim._

object SumSim {
  val spinalConfig = SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
    defaultClockDomainFrequency = FixedFrequency(10 Hz))

  def main(args: Array[String]) {
    val compiled = SimConfig.withConfig(spinalConfig)
      .withWave.compile(SumRAM(width = 8, size = 5))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      for (k <- 0 until 20) {
        dut.clockDomain.waitSampling()
      }
    }
  }
}
