package shs.ram

import spinal.core._

case class SumTester(size: Int = 10) extends Component {
  val io = new Bundle {
    val leds = out UInt(width = 8 bits)
  }

  val s1 = Sum(width = 8, size = size)

  when(s1.io.ready) {
    io.leds := s1.io.result
  } otherwise {
    io.leds := U(8 bits, default -> true)
  }
}

object SumMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/sum"
    ).generateVerilog(SumTester(5))
  }
}
