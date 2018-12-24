package obijuan.t22syncrules

import spinal.core._

object TxTest1Main {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/obijuan/t22e1"
    ).generateVerilog(TxTest1())
  }

}
