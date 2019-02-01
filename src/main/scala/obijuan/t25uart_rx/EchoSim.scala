package obijuan.t25uart_rx

import obijuan.lib._
import spinal.core._
import spinal.core.sim._

object EchoSim {

  val BAUDRATE = BaudGen.B115200
  val BITRATE = BAUDRATE * 10
  val FRAME = BITRATE * 10
  val FRAME_WAIT = BITRATE * 4

  def main(args: Array[String]): Unit = {
    val compiled = SimConfig
      .withConfig(SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT)))
      .withWave.compile(
      Echo(BAUDRATE))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      def send_byte(byte: Int): Unit = {
        dut.io.rx #= false
        for (i <- 0 until 8) {
          sleep(cycles = BITRATE)
          dut.io.rx #= ((byte >> i) & 0x1) == 1
          dut.clockDomain.waitSampling()
        }
        sleep(cycles = BITRATE)
        dut.io.rx #= true
        sleep(cycles = BITRATE)
        dut.io.rx #= true
      }

      sleep(cycles = BITRATE)
      send_byte(byte = 0x55.toInt)

      sleep(cycles = FRAME_WAIT * 3)
      send_byte(byte = 'K'.toInt)

      sleep(cycles = FRAME_WAIT * 4)
      println("End of simulation")
    }
  }
}
