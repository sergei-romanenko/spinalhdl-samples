package shs.min

import spinal.core._
import spinal.core.sim._
import spinal.sim._

object MinSim {
  val spinalConfig = SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
    defaultClockDomainFrequency = FixedFrequency(12 MHz))

  def main(args: Array[String]) {
    val compiled = SimConfig.withConfig(spinalConfig)
      .withWave.compile(Min(16, 5))
    compiled.doSim { dut =>
      val inputs = Seq(Vector(40, 20, 10, 50, 60), Vector(10, 20, 30, 40, 50))

      for (values <- inputs) {
        println(s"$values")
        for (i <- values.indices)
          dut.io.values(i) #= values(i)
        sleep(cycles = 10)
        val result = dut.io.result.toInt
        println(s"result=$result")
      }
    }
  }
}
