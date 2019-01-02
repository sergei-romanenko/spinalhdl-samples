package obijuan.t23fsmtx

import spinal.core._
import spinal.core.sim._
import spinal.sim._

object FsmTxSim {

  val BAUD = BaudGen.B115200
  val BITRATE = BAUD << 1
  val FRAME = BITRATE * 11
  val FRAME_WAIT = BITRATE * 4

  def main(args: Array[String]): Unit = {
    val compiled = SimConfig
      .withConfig(SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT)))
      .withWave.compile(
      FsmTx(BAUD = BAUD))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      sleep(cycles = 1)
      dut.io.dtr #= false

      sleep(cycles = FRAME_WAIT)
      dut.io.dtr #= true
      sleep(cycles = BITRATE * 2)
      dut.io.dtr #= false

      sleep(cycles = FRAME_WAIT * 2)
      dut.io.dtr #= true
      sleep(cycles = FRAME * 1)
      dut.io.dtr #= false

      sleep(cycles = FRAME_WAIT * 4)

      for (k <- 0 until 1000) {
        dut.clockDomain.waitSampling()
      }

      println("End of simulation")
    }
  }
}
