package obijuan.t17tones

import spinal.core._

object TonesMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl/obijuan/Tones")
      .generateVerilog(Tones()).printPruned()
  }
}
