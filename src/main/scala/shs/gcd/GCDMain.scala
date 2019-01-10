package shs.gcd

import spinal.core._

object GCDMain {

  val g = GCDGenerics(8)

  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/gcd"
    ).generateVerilog(GCD(g))
  }
}
