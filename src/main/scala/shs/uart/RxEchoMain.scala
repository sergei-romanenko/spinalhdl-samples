package shs.uart

import spinal.core._

object RxEchoMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/uart/rxecho"
    ).generateVerilog(RxEcho())
  }
}
