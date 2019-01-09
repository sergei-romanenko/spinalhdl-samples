package shs.gcd

import spinal.core._
import spinal.lib._

case class GCDGenerics(width: Int = 16) {
  def valueType = UInt(width bits)
}

case class GCDTask(g: GCDGenerics) extends Bundle {
  val a, b = g.valueType
}

case class GCDResult(g: GCDGenerics) extends Bundle {
  val a, b, r = g.valueType
}
