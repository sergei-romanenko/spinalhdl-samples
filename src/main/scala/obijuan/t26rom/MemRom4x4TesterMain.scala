package obijuan.t26rom

import spinal.core._

object MemRom4x4TesterMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      targetDirectory = "rtl/obijuan/t26e3mem"
    ).generateVerilog(MemRom4x4Tester(
      ROMFILE = "src/main/scala/obijuan/t26rom/rom1.mem"))
      .printPruned()
  }
}
