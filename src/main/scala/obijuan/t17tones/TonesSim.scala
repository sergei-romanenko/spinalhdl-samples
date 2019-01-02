package obijuan.t17tones

import spinal.core._
import spinal.core.sim._
import spinal.sim._

object TonesSim {
  def main(args: Array[String]): Unit = {
    val compiled = SimConfig
      .withConfig(SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT)))
      .withWave.compile(
      Tones(F0 = 3, F1 = 5, F2 = 7, F3 = 10))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)
      for (k <- 0 until 50) {
        dut.clockDomain.waitSampling()
      }
    }
  }
}
