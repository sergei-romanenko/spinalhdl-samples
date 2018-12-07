package obijuan.t26rom

import spinal.core._

object RomFile4x4TesterMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      targetDirectory = "rtl/obijuan/t26e3"
    ).generateVerilog(RomFile4x4Tester(
      ROMFILE = "src/main/scala/obijuan/t26rom/rom1.list"))
      .printPruned()
  }
}
