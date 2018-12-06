package obijuan.t17tones

import spinal.core._
import spinal.core.sim._
import spinal.sim._

//noinspection FieldFromDelayedInit,TypeAnnotation
object TonesSim {
  def main(args: Array[String]): Unit = {
    val compiled = SimConfig.withWave.compile(
      new Tones(F0 = 3, F1 = 5, F2 = 7, F3 = 10)/* {
        val dummy = Reg(Bool) init False}*/)
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)
      Suspendable.repeat(50) {
        dut.clockDomain.waitSampling()
      }
    }
  }
}
