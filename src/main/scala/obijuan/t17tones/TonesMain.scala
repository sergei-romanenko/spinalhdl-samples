package obijuan.t17tones

import spinal.core._

object TonesMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      targetDirectory = "rtl/obijuan/t17tones")
      .generateVerilog(Tones()).printPruned()
  }
}
