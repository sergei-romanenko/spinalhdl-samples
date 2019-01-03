package shs.pwm

import spinal.core._
import spinal.lib._

import shs.lib._

case class PWMTester
(
  frequency: HertzNumber = 100 kHz,
  limit: BigInt = 255,
  step: BigInt = 32
) extends Component {

  val io = new Bundle {
    val switch_up = in Bool
    val switch_dn = in Bool
    val leds = out UInt (8 bits)
  }

  def valueType = UInt(limit.bitLength bits)

  val s_up, s_dn = Bool

  val d1 = Debouncer()
  d1.io.switch_input := io.switch_up
  s_up := d1.io.trans_up
  val d2 = Debouncer()
  d2.io.switch_input := io.switch_dn
  s_dn := d2.io.trans_up

  val duty = Reg(valueType) init 0

  when(s_up && duty <= limit - step) {
    duty := duty + step
  }

  when(s_dn && duty >= step) {
    duty := duty - step
  }

  val pwm = PWM(limit, frequency)
  pwm.io.duty := duty

  io.leds := 0
  io.leds(0) := pwm.io.pwm_pin
}
