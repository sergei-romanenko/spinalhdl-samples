package ice40hx8k.counter

import spinal.core._

object CounterMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl/ice40hx8k/Counter").generateVerilog(Counter())
  }
}
