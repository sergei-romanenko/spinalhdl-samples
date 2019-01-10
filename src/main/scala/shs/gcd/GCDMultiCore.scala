package shs.gcd


import spinal.core._
import spinal.lib._
import shs.lib.{Arbiter, Dispatcher}

case class GCDMultiCore(g: GCDGenerics, coreCount: Int)
  extends Component {

  val io = new Bundle {
    val cmd = slave Stream GCDTask(g)
    val rsp = master Stream GCDResult(g)
  }

  val dispatcher = Dispatcher(GCDTask(g), coreCount)
  val arbiter = Arbiter(GCDResult(g), coreCount)

  dispatcher.io.input << io.cmd
  io.rsp << arbiter.io.output

  val solver = List.fill(coreCount)(GCD(g))

  for (i <- 0 until coreCount) {
    solver(i).io.cmd <-< dispatcher.io.outputs(i)
    arbiter.io.inputs(i) <-< solver(i).io.rsp
  }

}
