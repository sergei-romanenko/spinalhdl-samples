package obijuan.t15divider

import spinal.core._
import spinal.core.sim._
import spinal.sim._

//noinspection TypeAnnotation,FieldFromDelayedInit
object DivMSim {

  case class DivMDut() extends Component {
    val io = new Bundle {
      val tick = out Bool
    }

    val dummy = Reg(Bool) init False
    val dm = DivM(M = 6)
    io.tick := dm.io.tick
  }

  def main(args: Array[String]) {
    val compiled = SimConfig.withWave.compile(DivMDut())
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(period = 10)

      var cnt = 0

      Suspendable.repeat (50) {
        dut.clockDomain.waitSampling()
        //println(s"cnt=${cnt},tick = ${dut.io.tick.toBoolean}")
        val high = cnt >> 2
        assert(dut.io.tick.toBigInt == high)
        cnt = if (cnt == 5) 0 else cnt + 1
      }
    }
  }
}
