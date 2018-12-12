package shs.pwm

import spinal.core._
import spinal.lib._

import shs.lib._

//noinspection TypeAnnotation,LanguageFeature,ForwardReference,FieldFromDelayedInit
case class PWMTester(frequency: HertzNumber = 1 MHz) extends Component {
  val io = new Bundle {
    val switch_up = in Bool
    val switch_dn = in Bool
    val leds = out UInt (8 bits)
  }

  val max = 255
  val step = 32
  val width = log2Up(max + 1)

  val s_up, s_dn = Bool

  val d1 = Debouncer()
  d1.io.switch_input := io.switch_up
  s_up := d1.io.trans_up
  val d2 = Debouncer()
  d2.io.switch_input := io.switch_dn
  s_dn := d2.io.trans_up

  val duty = RegInit(U(0, width bits))

  when(s_up && duty <= max - step) {
    duty := duty + step
  }

  when(s_dn && duty >= step) {
    duty := duty - step
  }


  val pwm = PWM(max = max, frequency = frequency)
  pwm.io.duty := duty

  io.leds := 0
  io.leds(0) := pwm.io.pwm_pin
}
