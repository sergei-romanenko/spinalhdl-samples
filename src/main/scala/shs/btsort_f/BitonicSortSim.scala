package shs.btsort_f

import spinal.core._
import spinal.core.sim._
import spinal.sim._

object BitonicSortSim {
  val spinalConfig = SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
    defaultClockDomainFrequency = FixedFrequency(12 MHz))

  def main(args: Array[String]) {
    val compiled = SimConfig.withConfig(spinalConfig)
      .withWave.compile(BitonicSort(width = 8, size = 8))
    compiled.doSim { dut =>
      val inputs = Seq(
        Vector(40, 20, 70, 30, 10, 80, 50, 60),
        Vector(10, 20, 30, 40, 50, 60, 70, 80),
        Vector(80, 70, 60, 50, 40, 30, 20, 10),
        Vector(40, 40, 70, 60, 10, 70, 50, 60))

      for (values <- inputs) {
        println(s"values=$values")
        for (i <- values.indices)
          dut.io.values(i) #= values(i)
        sleep(cycles = 10)
        val results = dut.io.results.toVector.map(_.toInt)
        println(s"results=$results")
        require(values.sorted == results, "values.sorted != results")
      }
    }
  }
}
