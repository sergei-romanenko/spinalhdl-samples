package obijuan.t26rom

import spinal.core._

//noinspection TypeAnnotation
case class Divider(M: Long = Freq.T_100ms) extends Component {
  val io = new Bundle {
    val tick = out Bool
  }

  val N = log2Up(M)
  val max = U(M - 1)

//  val coreClockDomain = ClockDomain(
//    clockDomain.clock, config = ClockDomainConfig(resetKind = BOOT))
//
//  val clockingArea = new ClockingArea(coreClockDomain) {
    val cnt = RegInit(U(0, N bits))
    io.tick := cnt === 0
    cnt := (cnt === max) ? U(0) | (cnt + 1)
//  }
}
