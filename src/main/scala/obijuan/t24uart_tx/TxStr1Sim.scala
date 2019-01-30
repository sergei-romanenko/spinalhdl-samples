package obijuan.t24uart_tx

import obijuan.lib._
import spinal.core._
import spinal.core.sim._

object TxStr1Sim {

  val BAUDRATE = BaudGen.B115200
  val BITRATE = BAUDRATE << 1
  val FRAME = BITRATE * 11
  val FRAME_WAIT = BITRATE * 4

  def main(args: Array[String]): Unit = {
    val compiled = SimConfig
      .withConfig(SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT)))
      .withWave.compile(
      TxStr1(BAUDRATE = BAUDRATE))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      sleep(cycles = 1)
      dut.io.dtr #= false

      sleep(cycles = FRAME_WAIT)
      dut.io.dtr #= true
      sleep(cycles = BITRATE * 2)
      dut.io.dtr #= false

      sleep(cycles = FRAME * 11)
      dut.io.dtr #= true
      sleep(cycles = BITRATE * 1)
      dut.io.dtr #= false

      sleep(cycles = FRAME * 11)

      for (k <- 0 until 1000) {
        dut.clockDomain.waitSampling()
      }

      println("End of simulation")
    }
  }
}
