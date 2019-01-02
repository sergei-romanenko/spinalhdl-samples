package shs.sqrt

import spinal.core._
import spinal.sim._
import spinal.core.sim._

object SqrtPVSim {
  val spinalConfig = SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
    defaultClockDomainFrequency = FixedFrequency(12 MHz))

  def main(args: Array[String]) {
    val compiled = SimConfig.withConfig(spinalConfig)
      .withWave.compile(SqrtPV(4))
    compiled.doSim { dut =>
      val inputs = Seq(1, 2, 3, 4, 25, 27, 224, 225, 0, 0, 0, 0, 0, 0, 0)
      dut.clockDomain.forkStimulus(period = 10)

      for (value <- inputs) {
        dut.io.value #= value
        dut.clockDomain.waitSampling()
        val v = dut.io.rsp_value.toLong
        val r = dut.io.rsp_result.toLong
        println(s"value=$v, result=$r")
        assert(r * r <= v && (r + 1) * (r + 1) >= v)
      }
    }
  }
}
