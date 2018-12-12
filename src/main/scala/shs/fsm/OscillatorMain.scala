package shs.fsm

import spinal.core._

//noinspection TypeAnnotation,LanguageFeature,FieldFromDelayedInit
case class OscillatorTester
(
  N: Int = 8,
  frequency: HertzNumber = 1 Hz
)
  extends Component {
  val io = new Bundle {
    val leds = out UInt (N bits)
  }

  val oscillator = Oscillator(N, frequency)

  val width = log2Up(N)
  val ampl = UInt(width bits)
  ampl := oscillator.io.ampl

  io.leds := 0
  io.leds(ampl) := True
}


//noinspection LanguageFeature
object OscillatorMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/fsm"
    ).generateVerilog(OscillatorTester(8, 1 Hz))
  }
}
