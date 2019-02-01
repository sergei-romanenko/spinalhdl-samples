package shs.uart

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

import shs.lib._

case class TxStr
(
  BAUDRATE: Int = BaudGen.B115200,
  msg: String = "Hello!.."
) extends Component {

  val io = new Bundle {
    val dtr = in Bool
    val tx = out Bool
  }

  val enable = RegNext(!io.dtr) init False

  //  val valid = RegInit(False)
  //  val data = Reg(Bits(width = 8 bits))
  val valid = False
  val data = Bits(width = 8 bits)

  val utx = UartTx(BAUDRATE)
  utx.io.cmd.valid := valid
  utx.io.cmd.payload := data
  io.tx := utx.io.tx

  // Characters counter
  val cw = log2Up(msg.length)
  val count = Reg(UInt(cw bits)) init 0

  // Message

  /*
    val romContent: Seq[Bits] = msg.map(c => B(c.toByte, 8 bits))
    val rom = Mem(initialContent = romContent)

    data := rom.readSync(address = count)
  */

  val chars: Vector[Bits] = msg.map(c => B(c.toByte, 8 bits)).toVector

  switch(count) {
    for (i <- chars.indices) {
      is(i)(data := chars(i))
    }
  }

  val fsm = new StateMachine {
    val IDLE: State = new State with EntryPoint
    val NEXT, TRANS, STOP: State = new State

    IDLE.whenIsActive {
      when(enable) {
        count := 0
        goto(NEXT)
      }
    }

    NEXT.whenIsActive {
      when(utx.io.cmd.ready) {
        goto(TRANS)
      }
    }

    TRANS.whenIsActive {
      valid := True
      when(count === msg.length - 1) {
        goto(STOP)
      } otherwise {
        count := count + 1
        goto(NEXT)
      }
    }

    STOP.whenIsActive {
      when(!enable) {
        goto(IDLE)
      }
    }
  }
}

/*
module txstr #(
          parameter BAUDRATE = `B115200
)(
          input wire clk,   //-- System clock
          input wire rstn,  //-- Reset (active low)
          output wire tx    //-- Serial payload output
);


//-- Serial Unit instantation
uart_tx #(
    .BAUDRATE(BAUDRATE)  //-- Set the baudrate

  ) TX0 (
    .clk(clk),
    .rstn(rstn),
    .payload(payload),
    .valid(valid),
    .tx(tx),
    .ready(ready)
);

//-- Connecting wires
wire ready;
reg valid = 0;
reg [7:0] payload;

//-- Multiplexer with the 8-character string to transmit
always @*
  case (char_count)
    8'd0: payload <= "H";
    8'd1: payload <= "e";
    8'd2: payload <= "l";
    8'd3: payload <= "l";
    8'd4: payload <= "o";
    8'd5: payload <= "!";
    8'd6: payload <= ".";
    8'd7: payload <= ".";
    default: payload <= ".";
  endcase

//-- Characters counter
//-- It only counts when the cena control signal is enabled
reg [2:0] char_count;
reg cena;                //-- Counter enable

always @(posedge clk)
  if (!rstn)
    char_count = 0;
  else if (cena)
    char_count = char_count + 1;


//--------------------- CONTROLLER

localparam INI = 0;
localparam TXCHR = 1;
localparam NEXTCHR = 2;
localparam STOP = 3;

//-- fsm state
reg [1:0] state;
reg [1:0] next_state;

//-- Transition between states
always @(posedge clk) begin
  if (!rstn)
    state <= INI;
  else
    state <= next_state;
end

//-- Control signal generation and next states
always @(*) begin
  next_state = state;
  valid = 0;
  cena = 0;

  case (state)
    //-- Initial state. Start the trasmission
    INI: begin
      valid = 1;
      next_state = TXCHR;
    end

    //-- Wait until one car is transmitted
    TXCHR: begin
      if (ready)
        next_state = NEXTCHR;
    end

    //-- Increment the character counter
    //-- Finish when it is the last character
    NEXTCHR: begin
      cena = 1;
      if (char_count == 7)
        next_state = STOP;
      else
        next_state = INI;
    end

  endcase
end


endmodule
*/