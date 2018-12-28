package shs.sqrt

import spinal.core._
import spinal.core.sim._

object SqrtI2Sim {
  val spinalConfig = SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
    defaultClockDomainFrequency = FixedFrequency(12 MHz))

  def main(args: Array[String]) {
    val compiled = SimConfig.withConfig(spinalConfig)
      .withWave.compile(SqrtI2(4))
    compiled.doSim { dut =>
      val inputs = Vector(1, 2, 3, 4, 25, 27, 254, 255)
      dut.clockDomain.forkStimulus(period = 10)

      sleep(cycles = 10)
      var idx = 0
      while (idx < inputs.length) {
        val v = inputs(idx)
        sleep(cycles = 10)
        dut.io.cmd.payload #= v
        dut.io.cmd.valid #= true
        waitUntil(dut.io.rsp.valid.toBoolean)

        dut.io.rsp.ready #= true
        val r = dut.io.rsp.payload.toLong
        println(s"value=$v, result=$r")
        assert(r * r <= v && (r + 1) * (r + 1) >= v)
        dut.clockDomain.waitSampling()

        dut.io.cmd.valid #= false
        dut.io.rsp.ready #= false
        dut.clockDomain.waitSampling()
        idx += 1
      }
    }
  }
}
