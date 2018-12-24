package obijuan.t22syncrules

import spinal.core._
import spinal.core.sim._

object TxTest3Sim {
  val BAUD = BaudGen.B115200
  val BITRATE = (BAUD << 1) * 7
  val FRAME = BITRATE * 11

  def main(args: Array[String]): Unit = {
    val compiled = SimConfig
      .withConfig(SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT)))
      .withWave.compile(TxTest3(BAUD = BAUD, DELAY = 4000))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      sleep(cycles = FRAME * 6)

      println("END of simulation!")
    }
  }
}
