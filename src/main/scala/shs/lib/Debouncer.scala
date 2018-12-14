package shs.lib

import spinal.core._
import spinal.lib._

//noinspection TypeAnnotation,LanguageFeature,ForwardReference
case class Debouncer() extends Component {
  val io = new Bundle {
    val switch_input = in Bool
    val state = out Bool
    val trans_up = out Bool
    val trans_dn = out Bool
  }

  val state = RegInit(False)
  io.state := state

  // Synchronize the switch input to the clock
  val sync_0, sync_1 = RegInit(False)
  sync_0 := io.switch_input
  sync_1 := sync_0

  // Debounce the switch
  val count = RegInit(U(0, 17 bits))
  // true when all bits of count are 1's
  val finished = Bool
  finished := (~count === 0)

  val idle = Bool
  idle := (state === sync_1)

  io.trans_dn := ~idle & finished & ~state
  io.trans_up := ~idle & finished & state

  when(idle) {
    count := 0
  } otherwise {
    count := count + 1
    when(finished) {
      state := ~state
    }
  }

}
