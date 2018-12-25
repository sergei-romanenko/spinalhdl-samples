package shs.sqrt

import spinal.core._
import spinal.sim._
import spinal.core.sim._

object SqrtSim {
  val spinalConfig = SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
    defaultClockDomainFrequency = FixedFrequency(12 MHz))

  val g = SqrtGenerics(16)

  def main(args: Array[String]) {
    val compiled = SimConfig.withConfig(spinalConfig)
      .withWave.compile(Sqrt(g))
    compiled.doSim { dut =>
      val inputs = Vector(1, 2, 3, 4, 25, 27, 254, 255)
      dut.clockDomain.forkStimulus(period = 10)

      sleep(cycles = 10)
      var idx = 0
      while (idx < inputs.length) {
        val value = inputs(idx)
        sleep(cycles = 10)
        dut.io.cmd.value #= value
        dut.io.cmd.valid #= true
        waitUntil(dut.io.rsp.valid.toBoolean)

        dut.io.rsp.ready #= true
        val v = dut.io.rsp.value.toLong
        val r = dut.io.rsp.result.toLong
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
