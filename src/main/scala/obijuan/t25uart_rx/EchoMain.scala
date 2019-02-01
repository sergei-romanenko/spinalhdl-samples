package obijuan.t25uart_rx

import spinal.core._

object EchoMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/obijuan/t25uart-rx/echo"
    ).generateVerilog(Echo())
  }
}
