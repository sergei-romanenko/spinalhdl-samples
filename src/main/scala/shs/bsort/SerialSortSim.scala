package shs.bsort

import shs.sqrt.SqrtPR
import spinal.core._
import spinal.core.sim._
import spinal.sim._

object SerialSortSim {
  val spinalConfig = SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
    defaultClockDomainFrequency = FixedFrequency(12 MHz))

  def main(args: Array[String]) {
    val size = 5
    val compiled = SimConfig.withConfig(spinalConfig)
      .withWave.compile(SerialSort(dataWidth = 8, size = size))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      val inputs = Vector(5, 255, 118, 151, 1)

      dut.io.enable #= false
      dut.io.unsorted_data #= 0
      dut.clockDomain.waitSampling()

      dut.io.enable #= true
      dut.io.write #= true

      for(i <- 0 until size) {
        dut.io.unsorted_data #= inputs(i)  //.randomize()
        dut.clockDomain.waitSampling()
        println(s"$i ${dut.io.unsorted_data.toInt}")
      }

      println()

      dut.io.write #= false

      for(i <- 0 until size) {
        dut.clockDomain.waitSampling()
        println(s"$i ${dut.io.sorted_data.toInt}")
      }

      println()
      println("Finished!")
    }
  }
}
