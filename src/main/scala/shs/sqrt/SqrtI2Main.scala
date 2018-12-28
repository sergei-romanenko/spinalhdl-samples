package shs.sqrt

import spinal.core._

object SqrtI2Main {

  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/sqrt"
    ).generateVerilog(SqrtI2(16))
  }
}
