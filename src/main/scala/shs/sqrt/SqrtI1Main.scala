package shs.sqrt

import spinal.core._

object SqrtI1Main {

  val g = SqrtGenerics(16)

  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/sqrt"
    ).generateVerilog(SqrtI1(g))
  }
}
