package shs.uart

import shs.lib._
import spinal.core._
import spinal.core.sim._

object TxCharSim {
  val BAUDRATE = BaudGen.B115200
  val SERIAL_CHAR = BAUDRATE * 11 * 10

  def main(args: Array[String]): Unit = {
    val compiled = SimConfig
      .withConfig(SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT)))
      .withWave.compile(TxChar(BAUDRATE))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      sleep(cycles = SERIAL_CHAR * 3)
      println("END of simulation!")
    }
  }
}
