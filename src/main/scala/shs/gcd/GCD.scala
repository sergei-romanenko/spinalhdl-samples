package shs.gcd

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

case class GCD(g: GCDGenerics) extends Component {
  val io = new Bundle {
    val cmd = slave Stream GCDTask(g)
    val rsp = master Stream GCDResult(g)
  }

  import g._

  val x, y = Reg(valueType) init 0
  val result = Reg(GCDResult(g))
  val valid = RegInit(False)
  valid := False

  //Apply default assignment
  result.r := (x === 0) ? y | ((y === 0) ? x | 0)
  io.rsp.payload := result
  io.rsp.valid := valid
  io.cmd.ready := False

  val fsm = new StateMachine {
    val IDLE: State = new State with EntryPoint
    val BUSY: State = new State

    IDLE.whenIsActive {
      when(io.cmd.valid) {
        result.a := io.cmd.a
        result.b := io.cmd.b
        x := io.cmd.a
        y := io.cmd.b
        goto(BUSY)
      }
    }

    BUSY.whenIsActive {
      when(!io.cmd.valid) {
        goto(IDLE)
      } elsewhen (x =/= 0 && y =/= 0) {
        when(x >= y) {
          x := x - y
        } otherwise {
          y := y - x
        }
      } otherwise {
        valid := True
        when(io.rsp.ready) {
          io.cmd.ready := True
          goto(IDLE)
        }
      }
    }
  }
}
