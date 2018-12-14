package shs.pwm

import spinal.core._

object PWMTesterMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/pwm"
    ).generateVerilog(PWMTester(1 MHz))
  }
}
