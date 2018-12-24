package obijuan.t22syncrules

import spinal.core._

object TxTest3Main {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/obijuan/t22e3"
    ).generateVerilog(TxTest3())
  }

}
