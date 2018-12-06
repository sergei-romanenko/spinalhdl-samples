package obijuan.t26rom

import spinal.core._

object Rom4x4TesterMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      targetDirectory = "rtl/obijuan/t26e2"
    ).generateVerilog(Rom4x4Tester()).printPruned()
  }
}
