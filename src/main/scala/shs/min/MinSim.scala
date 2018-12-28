package shs.min

import shs.sqrt.SqrtPR
import spinal.core._
import spinal.core.sim._
import spinal.sim._

object MinSim {
  val spinalConfig = SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT),
    defaultClockDomainFrequency = FixedFrequency(12 MHz))

  def main(args: Array[String]) {
    val compiled = SimConfig.withConfig(spinalConfig)
      .withWave.compile(Min(16, 4))
    compiled.doSim { dut =>
      val inputs = Seq(Vector(40, 20, 10, 50), Vector(10, 20, 30, 40))
      dut.clockDomain.forkStimulus(period = 10)

      inputs.suspendable.foreach { value =>
        println(s"$value")
        for (i <- value.indices)
          dut.io.cmd.payload(i) #= value(i)
        dut.io.cmd.valid #= true
        Suspendable.repeat(times = 5) {
          dut.clockDomain.waitSampling()
          val valid = dut.io.rsp.valid.toBigInt
          val r = dut.io.rsp.payload.toInt
          println(s"valid=$valid, result=$r")
          for (i <- value.indices)
            dut.io.cmd.payload(i) #= 0
          dut.io.cmd.valid #= false
        }
      }
    }
  }
}
