package shs.fsm

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

case class Blink
(
  N: Int = 8, frequency: HertzNumber = 1 Hz
) extends Component {
  val io = new Bundle {
    val leds = out UInt (N bits)
  }

  val slowArea = new SlowArea(frequency) {
    val tick = RegInit(True)

    tick := ~tick

    io.leds := 0
    io.leds(0) := tick
  }
}

object BlinkMain {
  def main(args: Array[String]) {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
      defaultClockDomainFrequency = FixedFrequency(12 MHz),
      targetDirectory = "rtl/shs/fsm-blink"
    ).generateVerilog(Blink(8, 2 Hz))
  }
}

import spinal.sim._
import spinal.core.sim._

object BlinkSim {
  val spinalConfig = SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
    defaultClockDomainFrequency = FixedFrequency(10 Hz))

  def main(args: Array[String]) {
    val compiled = SimConfig.withConfig(spinalConfig).withWave
      .compile(Blink(N = 2, frequency = 2 Hz))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      Suspendable.repeat (100) {
        dut.clockDomain.waitSampling()
      }
    }
  }
}
