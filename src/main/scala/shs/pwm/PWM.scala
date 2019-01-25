package shs.pwm

import spinal.core._
import spinal.lib._

case class PWM(limit: BigInt, frequency: HertzNumber) extends Component {
  def valueType = UInt(limit.bitLength bits)

  val io = new Bundle {
    val duty = in(valueType)
    val pwm_pin = out Bool
  }

  val slowArea = new SlowArea(frequency) {
    val on = RegInit(True)
    val cnt = Reg(valueType) init 0

    cnt := (cnt === limit - 1) ? U(0) | (cnt + 1)
    io.pwm_pin := (cnt < io.duty)
  }
}
