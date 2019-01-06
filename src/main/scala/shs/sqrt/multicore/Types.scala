package shs.sqrt.multicore

import spinal.core._
import spinal.lib._

case class SqrtGenerics(r_width: Int = 16) {
  val v_width = r_width * 2

  def valueType = UInt(v_width bits)

  def resultType = UInt(r_width bits)
}

case class SqrtTask(g: SqrtGenerics) extends Bundle {
  val value = g.valueType
}

case class SqrtResult(g: SqrtGenerics) extends Bundle {
  val value = g.valueType
  val result = g.resultType
}
