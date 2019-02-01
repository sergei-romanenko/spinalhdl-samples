package shs.uart

import spinal.core._

object RxLedsMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/uart/rxleds"
    ).generateVerilog(RxLeds())
  }
}
