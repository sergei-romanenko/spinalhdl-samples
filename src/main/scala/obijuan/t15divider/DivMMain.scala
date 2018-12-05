package obijuan.t15divider

import spinal.core._

object DivMMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl/obijuan/DivM").generateVerilog(DivM())
  }
}
