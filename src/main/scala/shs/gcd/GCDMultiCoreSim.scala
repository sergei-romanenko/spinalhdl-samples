package shs.gcd

import spinal.core._
import spinal.sim._
import spinal.core.sim._

object GCDMultiCoreSim {
  val spinalConfig = SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
    defaultClockDomainFrequency = FixedFrequency(12 MHz))

  val g = GCDGenerics(8)

  def main(args: Array[String]) {
    val compiled = SimConfig.withConfig(spinalConfig).withWave
      .compile(GCDMultiCore(g, 3))
    compiled.doSimUntilVoid { dut =>

      dut.clockDomain.forkStimulus(period = 10)
      SimTimeout(2000 * 10)

      val pushFork = fork {
        while (true) {
          dut.io.cmd.a #= 0
          dut.io.cmd.b #= 0
          dut.io.cmd.valid #= false
          dut.clockDomain.waitSampling()
          dut.io.cmd.a.randomize()
          dut.io.cmd.b.randomize()
          dut.io.cmd.valid #= true
          dut.clockDomain.waitSampling()
          println(s"pushFork: ${dut.io.cmd.a.toLong}, ${dut.io.cmd.b.toLong}")
          waitUntil(dut.io.cmd.ready.toBoolean)
        }
      }

      val popFork = fork {
        dut.io.rsp.ready #= false
        dut.clockDomain.waitSampling()
        dut.io.rsp.ready #= true
        for (i <- 0 until 1000) {
          dut.io.cmd.ready #= false
          dut.clockDomain.waitSampling()
          if (dut.io.rsp.valid.toBoolean) {
            val a = dut.io.rsp.a.toLong
            val b = dut.io.rsp.b.toLong
            val r = dut.io.rsp.r.toLong
            println(s"a=$a, b=$b, r=$r")
            assert(GCDFN.gcdRec(a, b) == r)
            dut.io.cmd.ready #= true
            dut.clockDomain.waitSampling()
          }
        }
        simSuccess()
      }

      popFork.join()
    }

  }
}
