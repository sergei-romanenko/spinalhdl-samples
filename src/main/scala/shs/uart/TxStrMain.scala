package shs.uart

import spinal.core._

object TxStrMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      targetDirectory = "rtl/shs/uart/txstr"
    ).generateVerilog(TxStr())
      .printPruned()
  }
}
