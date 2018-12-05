package obijuan.t04counter

import spinal.core._
import spinal.sim._
import spinal.core.sim._

//noinspection FieldFromDelayedInit
object CounterSim {
  def main(args: Array[String]) {
    val compiled = SimConfig.withWave.compile(Counter(width = 6, nleds = 4))
    //SimConfig.withWave.doSim(Counter(WIDTH = 6, NLEDS = 4)) { dut =>
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      var cnt = 0

      Suspendable.repeat (100) {
        dut.clockDomain.waitSampling()
        val high = cnt >> (dut.width - dut.nleds)
        assert(dut.io.leds.toInt == high)
        cnt = (cnt + 1) & 0x3F
      }
    }
  }
}
