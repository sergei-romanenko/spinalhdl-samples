package obijuan.t24uart_tx

import spinal.core._

object TxStr2Main {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/obijuan/t24uart-tx/txstr2"
    ).generateVerilog(TxStr2())
  }
}
