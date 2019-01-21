package obijuan.t27romparam

import spinal.core._
import obijuan.lib._
import obijuan.t26rom._

case class Player
(
  dur: Int = Freq.T_200ms,
  romfile: String = "imperial.mem",
  dw: Int = 16
) extends Component {

  val io = new Bundle {
    val leds = out UInt(8 bits)
    val out_ch = out Bool
  }

  val contents: Array[BigInt] = Tools.readmemh(romfile)
  val aw = log2Up(contents.length)
  val rom = new Mem(
    wordType = UInt(width = dw bits), wordCount = contents.length)
  rom.initialContent = contents

  val div = DividerP1(dur)
  val tick = Bool
  tick := div.io.tick

  val start = RegInit(True)
  start := False

  val addr = RegInit(U(0, aw bits))
  //val note = RegInit(U(contents(0), dw bits))
  val note = UInt(dw bits)
  note := start ? U(0, dw bits) | rom.readSync(address = addr)

  when(!start & tick) {
    addr := addr + 1
  }

  val ng = NoteGen(dw)
  ng.io.start := start
  ng.io.note := note

  io.leds := note(7 downto 0)
  io.out_ch := ng.io.out_ch
}
