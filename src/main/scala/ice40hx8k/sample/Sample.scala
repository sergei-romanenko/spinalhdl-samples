/*
 * SpinalHDL
 * Copyright (c) Dolu, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package ice40hx8k.sample

import spinal.core._
import spinal.lib._

import scala.util.Random

//noinspection TypeAnnotation
//Hardware definition
case class Sample() extends Component {
  val io = new Bundle {
    val cond0 = in  Bool
    val cond1 = in  Bool
    val flag  = out Bool
    val state = out UInt(8 bits)
  }
  val counter = RegInit(U(0, 8 bits))

  when(io.cond0){
    counter := counter + 1
  }

  io.state := counter
  io.flag  := (counter === 0) | io.cond1
}

//Generate the MyTopLevel's Verilog
object SampleMainVerilog {
  def main(args: Array[String]) {
    SpinalVerilog(Sample())
  }
}

//Generate the MyTopLevel's VHDL
object SampleMainVhdl {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl/ice40hx8k/Sample").generateVerilog(Sample())

  }
}


//Define a custom SpinalHDL configuration with synchronous reset instead of the default asynchronous one. This configuration can be resued everywhere
object SampleVerilogConfig extends SpinalConfig(
  targetDirectory = "rtl/ice40hx8k/Sample",
  defaultConfigForClockDomains = ClockDomainConfig(resetKind = SYNC))

//Generate the MyTopLevel's Verilog using the above custom configuration.
object SamplelVerilogWithCustomConfig {
  def main(args: Array[String]) {
    SampleVerilogConfig.generateVerilog(new Sample)
  }
}