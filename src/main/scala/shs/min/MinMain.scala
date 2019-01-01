package shs.min

import spinal.core._

object MinMain {

  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/min"
    ).generateVerilog(Min(16, 5))
  }
}
