package shs.uart

import spinal.core._

object TxCharMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      targetDirectory = "rtl/shs/uart/txchar"
    ).generateVerilog(TxChar())
      .printPruned()
  }
}
