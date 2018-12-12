package shs.pwm

import spinal.core._
import spinal.lib._

//noinspection TypeAnnotation,LanguageFeature,ForwardReference
case class PWM(max: Int, frequency: HertzNumber) extends Component {
  val width = log2Up(max + 1)
  val io = new Bundle {
    val duty = in UInt (width bits)
    val pwm_pin = out Bool
  }

  val slowArea = new SlowArea(frequency) {
    val on = RegInit(True)
    val cnt = RegInit(U(0, width bits))

    cnt := cnt + 1
    on := (cnt < io.duty)
    io.pwm_pin := (cnt < io.duty) //on
  }
}
