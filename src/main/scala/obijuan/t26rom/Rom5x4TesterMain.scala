package obijuan.t26rom

import spinal.core._

object Rom5x4TesterMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      targetDirectory = "rtl/obijuan/t26e1")
      .generateVerilog(Rom5x4Tester()).printPruned()
  }
}
