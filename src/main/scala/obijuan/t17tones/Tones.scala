package obijuan.t17tones

import spinal.core._
import spinal.lib._
import obijuan.lib.Freq

//noinspection TypeAnnotation,FieldFromDelayedInit,LanguageFeature
case class Tones
(
  F0: Long = Freq.F_1KHz,
  F1: Long = Freq.F_2KHz,
  F2: Long = Freq.F_3KHz,
  F3: Long = Freq.F_4KHz
) extends Component {
  val io = new Bundle {
    val ch0, ch1, ch2, ch3 = out Bool
  }

  val d0 = ToneGen(F0)
  io.ch0 := d0.io.tick
  val d1 = ToneGen(F1)
  io.ch1 := d1.io.tick
  val d2 = ToneGen(F2)
  io.ch2 := d2.io.tick
  val d3 = ToneGen(F3)
  io.ch3 := d3.io.tick
}
