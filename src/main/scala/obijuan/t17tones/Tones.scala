package obijuan.t17tones

import spinal.core._
import spinal.lib._

import Frequence._
//import obijuan.t17tones.Frequence._

//noinspection TypeAnnotation,FieldFromDelayedInit
case class Tones
(
  F0: Long = F_1KHz,
  F1: Long = Frequence.F_2KHz,
  F2: Long = Frequence.F_3KHz,
  F3: Long = Frequence.F_4KHz
) extends Component {
  val io = new Bundle {
    val ch0, ch1, ch2, ch3 = out Bool
  }

//  val dummy = Reg(Bool) init False
  val dummy = RegInit(False)

  val coreClockDomain = ClockDomain(
    clockDomain.clock, config = ClockDomainConfig(resetKind = BOOT))

  val clockingArea = new ClockingArea(coreClockDomain) {
    val d0 = Divider(F0)
    io.ch0 := d0.io.tick
    val d1 = Divider(F1)
    io.ch1 := d1.io.tick
    val d2 = Divider(F2)
    io.ch2 := d2.io.tick
    val d3 = Divider(F3)
    io.ch3 := d3.io.tick
    }
  }
