package obijuan.t22syncrules

import spinal.core._
import spinal.core.sim._
import spinal.sim._

object TxTest1Sim {
  val BAUD = BaudGen.B115200
  val BITRATE = (BAUD << 1) * 7
  val FRAME = BITRATE * 11
  val FRAME_WAIT = BITRATE * 2

  def main(args: Array[String]): Unit = {
    val compiled = SimConfig
      .withConfig(SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT)))
      .withWave.compile(TxTest1(BAUD = BAUD))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      dut.io.dtr #= false

      sleep(cycles = FRAME_WAIT)
      dut.io.dtr #= true
      sleep(cycles = FRAME)
      dut.io.dtr #= false

      sleep(cycles = FRAME_WAIT)
      dut.io.dtr #= true
      sleep(cycles = FRAME)
      dut.io.dtr #= false

      sleep(cycles = FRAME_WAIT)

      println("END of simulation!")
    }
  }
}
