package shs.bsort

import spinal.core._

object SerialSortMain {

  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/bsort"
    ).generateVerilog(SerialSort(dataWidth = 8, size = 3))
  }
}
