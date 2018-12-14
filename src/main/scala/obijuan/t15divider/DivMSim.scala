package obijuan.t15divider

import spinal.core._
import spinal.core.sim._
import spinal.sim._

object DivMSim {

  def main(args: Array[String]) {
    val compiled = SimConfig
      .withConfig(SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT)))
      .withWave
      .compile(DivM(M = 6))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      var cnt = 0

      Suspendable.repeat (50) {
        dut.clockDomain.waitSampling()
        //println(s"cnt=${cnt},tick = ${dut.io.tick.toBoolean}")
        val high = cnt >> 2
        assert(dut.io.tick.toBigInt == high)
        cnt = if (cnt == 5) 0 else cnt + 1
      }
    }
  }
}
