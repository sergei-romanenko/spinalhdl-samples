package shs.sqrt.multicore

import spinal.core._
import spinal.lib._
import shs.lib.{Arbiter, Dispatcher}

case class SqrtMultiCore(g: SqrtGenerics, coreCount: Int)
  extends Component {

  val io = new Bundle {
    val cmd = slave Stream SqrtTask(g)
    val rsp = master Stream SqrtResult(g)
  }

  val dispatcher = Dispatcher(SqrtTask(g), coreCount)
  val arbiter = Arbiter(SqrtResult(g), coreCount)

  dispatcher.io.input << io.cmd
  io.rsp << arbiter.io.output

  val solver = List.fill(coreCount)(Sqrt(g))

  for (i <- 0 until coreCount) {
    solver(i).io.cmd <-< dispatcher.io.outputs(i)
    arbiter.io.inputs(i) <-< solver(i).io.rsp
  }

}
