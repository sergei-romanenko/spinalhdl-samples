package shs.sqrt.multicore

import spinal.core._
import spinal.sim._
import spinal.core.sim._

object SqrtSim {
  val spinalConfig = SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
    defaultClockDomainFrequency = FixedFrequency(12 MHz))

  val g = SqrtGenerics(8)

  def main(args: Array[String]) {
    val compiled = SimConfig.withConfig(spinalConfig).withWave
      .compile(SqrtMultiCore(g, coreCount = 3))
    compiled.doSimUntilVoid { dut =>

      dut.clockDomain.forkStimulus(period = 10)
      SimTimeout(1000 * 10)

      val pushFork = fork {
        while (true) {
          dut.io.cmd.value #= 0
          dut.io.cmd.valid #= false
          dut.clockDomain.waitSampling()
          dut.io.cmd.value.randomize()
          dut.io.cmd.valid #= true
          dut.clockDomain.waitSampling()
          waitUntil(dut.io.cmd.ready.toBoolean)
        }
      }

      val popFork = fork {
        dut.io.rsp.ready #= false
        dut.clockDomain.waitSampling()
        dut.io.rsp.ready #= true
        for (i <- 0 until 50) {
          if(dut.io.rsp.valid.toBoolean) {
            val v = dut.io.rsp.value.toLong
            val r = dut.io.rsp.result.toLong
            println(s"value=$v, result=$r")
            assert(r * r <= v && (r + 1) * (r + 1) >= v)
          }
          dut.clockDomain.waitSampling()
        }
        simSuccess()
      }

      popFork.join()
    }

  }
}
