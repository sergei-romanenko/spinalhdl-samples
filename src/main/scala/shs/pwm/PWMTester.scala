package shs.pwm

import spinal.core._
import spinal.lib._

import shs.lib._

case class PWMTester
(
  frequency: HertzNumber = 100 kHz,
  limit: BigInt = 255
) extends Component {

  val io = new Bundle {
    val btn_incr = in Bool
    val btn_decr = in Bool
    val leds = out UInt (8 bits)
  }

  def valueType = UInt(limit.bitLength bits)

  val incr, decr = Bool

  val d1 = Debouncer()
  d1.io.btn_input := io.btn_incr
  incr := d1.io.trans_up
  val d2 = Debouncer()
  d2.io.btn_input := io.btn_decr
  decr := d2.io.trans_up

  val duty = Reg(valueType) init 0

  when(incr) {
    duty := (duty |<< 1) + 1
  }

  when(decr) {
    duty := duty |>> 1
  }

  val pwm = PWM(limit, frequency)
  pwm.io.duty := duty

  io.leds := 0
  io.leds(0) := pwm.io.pwm_pin
}
