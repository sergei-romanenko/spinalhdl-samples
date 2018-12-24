package obijuan.t22syncrules

import spinal.core._

object TxTest2Main {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/obijuan/t22e2"
    ).generateVerilog(TxTest2())
  }

}
