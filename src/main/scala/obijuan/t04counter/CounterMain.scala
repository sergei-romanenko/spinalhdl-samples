package obijuan.t04counter

import spinal.core._

object CounterMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl/obijuan/t04counter").generateVerilog(Counter())
  }
}
