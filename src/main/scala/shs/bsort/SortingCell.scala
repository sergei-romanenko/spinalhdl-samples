package shs.bsort

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

case class SortingCell(dataWidth: Int = 8) extends Component {
  def dataType = UInt(dataWidth bits)

  val io = new Bundle {
    val enable = in Bool
    val prev_is_pushed = in Bool
    val prev_empty = in Bool
    val shift_up = in Bool
    val prev_data = in(dataType)
    val new_data = in(dataType)
    val next_data = in(dataType)
    val is_pushed = out Bool
    val empty = out Bool
    val data = out(dataType)
  }

  val cell_empty = RegInit(True)
  io.empty := cell_empty
  val cell_data = Reg(dataType) init 0
  io.data := cell_data

  val new_data_fits = Bool
  new_data_fits := cell_empty || (io.new_data < cell_data)

  io.is_pushed := (!cell_empty) && new_data_fits

  when(io.enable && cell_empty) {
    cell_empty := !io.prev_is_pushed && io.prev_empty
  }

  when(io.enable) {
    when(io.shift_up) {
      cell_data := io.next_data
    } elsewhen (io.prev_is_pushed) {
      cell_data := io.prev_data
    } elsewhen (new_data_fits && !io.prev_is_pushed && !cell_empty) {
      cell_data := io.new_data
    } elsewhen (!io.prev_is_pushed && cell_empty && !io.prev_empty) {
      cell_data := io.new_data
    }
  }
}
