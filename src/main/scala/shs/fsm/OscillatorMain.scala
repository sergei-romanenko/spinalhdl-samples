package shs.fsm

import spinal.core._
import shs.lib._

import shs.lib.Freq

//noinspection TypeAnnotation,LanguageFeature,FieldFromDelayedInit
case class OscillatorTester
(
  N: Int = 8,
  factor: BigInt = Freq.F_1Hz
)
  extends Component {
  val io = new Bundle {
    val leds = out UInt (N bits)
  }

  val oscillator = Oscillator(N, factor)

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
      targetDirectory = "rtl/shs/fsm"
    ).generateVerilog(OscillatorTester(8, Freq.F_1Hz))
  }
}
