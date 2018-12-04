package ice40hx8k.combinational

import spinal.core._

object CombinationalMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl/ice40hx8k/Combinational").generateVerilog(Combinational())
  }

}
