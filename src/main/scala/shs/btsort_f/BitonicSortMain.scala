package shs.btsort_f

import spinal.core._

object BitonicSortMain {

  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/btsort_f"
    ).generateVerilog(BitonicSort(width = 16, size = 4))
  }
}
