package shs.sqrt

// This example is based on
// http://iverilog.wikia.com/wiki/Simulation

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
  val result = g.resultType
}


case class Sqrt(g: SqrtGenerics) extends Component {
  val io = new Bundle {
    val cmd = slave Stream SqrtTask(g)
    val rsp = master Stream SqrtResult(g)
  }

  import g._

  // Keep track of which bit I'm working on.
  val l_width = log2Up(r_width)
  val l = Reg(UInt(l_width bits))
  val i = Reg(UInt(l_width + 1 bits))

  val b = resultType
  b := U(1) << l
  val sq_b = valueType
  sq_b := U(1) << (l << 1)

  // `acc` holds the accumulated result, and `acc2` is
  //  the accumulated square of the accumulated result.
  val acc = Reg(resultType)
  val acc2 = Reg(valueType)

  // guess holds the potential next values for acc,
  // and guess2 holds the square of that guess.
  val guess = resultType
  guess := acc | b
  val guess2 = valueType
  guess2 := acc2 + sq_b + ((acc << l) << 1)

  //Apply default assignement
  io.cmd.ready := False
  io.rsp.valid := False
  io.rsp.result := acc

  def clear(): Unit = {
    io.cmd.ready := True
    acc := 0
    acc2 := 0
    l := U(r_width - 1)
    i := U(r_width)
  }

  val ini = RegInit(True)

  when(ini) {
    ini := False
    clear()
  } otherwise {
    when(io.cmd.valid) {
      //Is the sqrt iteration done?
      when(i === 0) {
        io.rsp.valid := True
        when(io.rsp.ready) {
          clear()
        }
      } otherwise {
        when(guess2 <= io.cmd.value) {
          acc := guess
          acc2 := guess2
        }
        l := l - 1
        i := i - 1
      }
    }
  }
}

/*
module sqrt(
  input  clk,
  input  reset,
  input [31:0] x,
  output valid,
  output [15:0] y
);

// acc holds the accumulated result, and acc2 is
//  the accumulated square of the accumulated result.
reg [15:0] acc;
reg [31:0] acc2;

// Keep track of which bit I'm working on.
reg [4:0]  bitl;
wire [15:0] bit = 1 << bitl;
wire [31:0] bit2 = 1 << (bitl << 1);

// The output is ready when the bitl counter underflows.
assign valid = bitl[4];
//assign valid = bitl == 0;
assign y = acc;

// guess holds the potential next values for acc,
// and guess2 holds the square of that guess.
wire [15:0] guess  = acc | bit;
wire [31:0] guess2 = acc2 + bit2 + ((acc << bitl) << 1);

task clear;
   begin
      acc = 0;
      acc2 = 0;
      bitl = 15;
   end
endtask

initial clear;

always @(reset or posedge clk)
   if (reset)
    clear;
   else begin
      if (guess2 <= x) begin
         acc  <= guess;
         acc2 <= guess2;
      end
      bitl <= bitl - 1;
   end

endmodule
*/