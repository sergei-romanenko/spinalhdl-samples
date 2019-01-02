package obijuan.t27romparam

import spinal.core._
import spinal.core.sim._
import spinal.sim._

object PlayerSim {
  def main(args: Array[String]): Unit = {
    val compiled = SimConfig
      .withConfig(SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT)))
      .withWave.compile(
      Player(dur = 4, romfile = "src/main/scala/obijuan/t27romparam/imperial.list"))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)
      for (k <- 0 until 200) {
        dut.clockDomain.waitSampling()
      }
    }
  }
}
