package shs.gcd

import spinal.core._
import spinal.sim._
import spinal.core.sim._

object OneCoreSim {
  val spinalConfig = SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
    defaultClockDomainFrequency = FixedFrequency(12 MHz))

  val g = GCDGenerics(8)

  def main(args: Array[String]) {
    val compiled = SimConfig.withConfig(spinalConfig)
      .withWave.compile(GCD(g))
    compiled.doSim { dut =>
      val inputs = Seq(
        (0, 0),
        (0, 5),
        (5, 0),
        (21, 49),
        (25, 30),
        (19, 27),
        (40, 40),
        (250, 190),
        (50, 250))

      dut.clockDomain.forkStimulus(period = 10)
      sleep(cycles = 10)

      for ((u, v) <- inputs) {
        dut.io.cmd.a #= u
        dut.io.cmd.b #= v
        dut.io.cmd.valid #= true
        dut.io.rsp.ready #= true
        dut.clockDomain.waitSampling()
        waitUntil(dut.io.rsp.valid.toBoolean)

        val a = dut.io.rsp.a.toLong
        val b = dut.io.rsp.b.toLong
        val r = dut.io.rsp.r.toLong
        println(s"a=$a, b=$b, r=$r")
        assert(GCDFN.gcdRec(a, b) == r)
        dut.io.cmd.valid #= false
        dut.clockDomain.waitSampling()
      }
    }
  }
}
