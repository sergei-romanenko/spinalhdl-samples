package obijuan.t23fsmtx

import spinal.core._

object FsmTxMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/obijuan/t23e1"
    ).generateVerilog(FsmTx())
  }
}
