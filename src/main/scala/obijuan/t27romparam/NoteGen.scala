package obijuan.t27romparam

import spinal.core._
import obijuan.lib._

//noinspection TypeAnnotation,LanguageFeature,FieldFromDelayedInit
case class NoteGen(dw: Int = 16) extends Component {
  val io = new Bundle {
    val start = in Bool
    val note = in UInt (dw bits)
    val out_ch = out Bool
  }

  val cnt = RegInit(U(0, dw bits))

  when(io.start || io.note === 0 || cnt === io.note - 1) {
    cnt := 0
  } otherwise {
    cnt := cnt + 1
  }

  val pulse = RegInit(False)
  io.out_ch := pulse

  when(io.start || io.note === 0 ) {
    pulse := False
  } elsewhen(cnt === 0) {
    pulse := !pulse
  }
}
