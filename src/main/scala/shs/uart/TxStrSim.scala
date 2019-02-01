package shs.uart

import spinal.core._
import spinal.core.sim._
import spinal.sim._

import shs.lib._

object TxStrSim {
  val BAUDRATE = BaudGen.B115200
  val BITRATE = BAUDRATE * 5
  val FRAME = BITRATE * 11
  val FRAME_WAIT = BITRATE * 4

  def main(args: Array[String]): Unit = {
    val compiled = SimConfig
      .withConfig(SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT)))
      .withWave.compile(TxStr(BAUDRATE, msg = "1234"))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      dut.io.dtr #= true
      sleep(cycles = FRAME * 1)
      dut.clockDomain.waitSampling()

      for(i <- 0 until 5000) {
        dut.io.dtr #= false
        //sleep(cycles = 10)
        dut.clockDomain.waitSampling()
      }

      println("END of simulation!")
    }
  }
}
