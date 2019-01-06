package shs.sqrt

import spinal.core._

object SqrtIMain {

  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/sqrt"
    ).generateVerilog(SqrtI(16))
  }
}
