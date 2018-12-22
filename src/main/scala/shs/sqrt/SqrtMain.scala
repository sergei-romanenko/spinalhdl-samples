package shs.sqrt

import spinal.core._

object SqrtMain {

  val g = SqrtGenerics(16)

  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/sqrt"
    ).generateVerilog(Sqrt(g))
  }
}
