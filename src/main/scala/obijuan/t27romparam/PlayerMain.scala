package obijuan.t27romparam

import spinal.core._

object PlayerMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      targetDirectory = "rtl/obijuan/t27romparam"
    ).generateVerilog(Player(
      romfile = "src/main/scala/obijuan/t27romparam/imperial.list"))
      .printPruned()
  }
}
