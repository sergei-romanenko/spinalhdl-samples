package ice40hx8k.divm

import spinal.core._

object DivMMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl/ice40hx8k/DivM").generateVerilog(DivM())
  }
}
