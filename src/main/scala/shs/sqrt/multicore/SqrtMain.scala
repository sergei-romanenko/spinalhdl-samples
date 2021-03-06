package shs.sqrt.multicore

import spinal.core._

object SqrtMain {

  val g = SqrtGenerics(8)

  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/sqrt-multicore"
    ).generateVerilog(SqrtMultiCore(g, 3))
  }
}
