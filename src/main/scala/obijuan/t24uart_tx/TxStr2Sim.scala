package obijuan.t24uart_tx

import obijuan.lib._
import spinal.core._
import spinal.core.sim._

object TxStr2Sim {

  val BAUDRATE = BaudGen.B115200
  val DELAY = 10000
  val BITRATE = BAUDRATE << 1
  val FRAME = BITRATE * 11
  val FRAME_WAIT = BITRATE * 4

  def main(args: Array[String]): Unit = {
    val compiled = SimConfig
      .withConfig(SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT)))
      .withWave.compile(
      TxStr2(BAUDRATE = BAUDRATE, DELAY = DELAY))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      sleep(cycles = FRAME * 20)

      for (k <- 0 until 10000) {
        dut.clockDomain.waitSampling()
      }

      println("End of simulation")
    }
  }
}
