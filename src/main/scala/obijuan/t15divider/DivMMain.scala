package obijuan.t15divider

import spinal.core._

object DivMMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      targetDirectory = "rtl/obijuan/t15divider"
    ).generateVerilog(DivM())
  }
}
