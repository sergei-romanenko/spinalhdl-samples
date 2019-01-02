package obijuan.t26rom

import spinal.core._
import spinal.core.sim._
import spinal.sim._

object MemRom4x4TesterSim {
  def main(args: Array[String]): Unit = {
    val compiled = SimConfig
      .withConfig(SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT)))
      .withWave.compile(
      MemRom4x4Tester(DELAY = 2, ROMFILE = "src/main/scala/obijuan/t26rom/rom1.list"))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)
      for (k <- 0 until 50) {
        dut.clockDomain.waitSampling()
      }
    }
  }
}
