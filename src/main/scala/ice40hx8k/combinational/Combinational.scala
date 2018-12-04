package ice40hx8k.combinational

import spinal.core._
import spinal.lib._

//noinspection TypeAnnotation
case class Combinational() extends Component {
  val io = new Bundle {
    val a = in Bool
    val b = in Bool
    val c = in Bool
    val result = out Bool

  }

  val a_and_b = io.a & io.b
  val not_c = !io.c
  io.result := a_and_b | not_c
}
