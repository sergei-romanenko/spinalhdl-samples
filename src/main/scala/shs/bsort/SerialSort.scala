package shs.bsort

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

// `dataWidth` defines the width of the data being stored.
// `enable` enables internal logic.
// `write` indicates if unsorted data is being written or sorted data is
//    being read out while module is enabled.
// `unsorted_data` serial input for unsorted list elements.
// `sorted_data` serial output for sorted list elements.

case class SerialSort(dataWidth: Int = 8, size: Int = 3) extends Component {
  def dataType = UInt(dataWidth bits)

  val io = new Bundle {
    val enable = in Bool
    val write = in Bool
    val unsorted_data = in(dataType)
    val sorted_data = out(dataType)
  }

  // Generate the chain of cell elements.

  val cell = Vector.fill(size)(SortingCell(dataWidth))

  for (k <- 0 until size) {
    cell(k).io.enable :=
      io.enable
    cell(k).io.prev_is_pushed :=
      (if (k == 0) False else cell(k - 1).io.is_pushed)
    cell(k).io.prev_empty :=
      (if (k == 0) False else cell(k - 1).io.empty)
    cell(k).io.shift_up :=
      !io.write
    cell(k).io.prev_data :=
      (if (k == 0) U(0) else cell(k - 1).io.data)
    cell(k).io.new_data :=
      io.unsorted_data
    cell(k).io.next_data :=
      (if (k + 1 == size) U(0) else cell(k + 1).io.data)
  }

  io.sorted_data := cell(0).io.data
}
