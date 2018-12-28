package shs.sqrt

import spinal.core._

case class SqrtPRTester() extends Component {
  val io = new Bundle {
    val leds = out UInt(width = 8 bits)
  }

  val sqrt = SqrtPR(8)
  sqrt.io.value := U"16'hFFFF"

  // ??? := sqrt.io.rsp_value
  io.leds := sqrt.io.rsp_result
}

object SqrtPRMain {

  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/sqrt"
    ).generateVerilog(SqrtPRTester()).printPruned()
  }
}
