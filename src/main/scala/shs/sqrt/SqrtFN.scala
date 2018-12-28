package shs.sqrt

// A von Neumann algorithm!

object SqrtFN {

  def sqrt(w: Int = 16, v: Long): Int = {
    var acc: Long = 0
    for (l <- (w - 1) to 0 by -1) {
      val b = 1L << l
      val guess = acc + b
      if (guess * guess <= v)
        acc = guess
    }
    acc.toInt
  }

  def sqrtOpt(w: Int = 16, v: Long): Int = {
    var acc: Long = 0
    var acc2: Long = 0
    for (l <- (w - 1) to 0 by -1) {
      val b = 1L << l
      val b2 = 1L << (l << 1)
      val guess = acc | b
      val guess2 = acc2 + b2 + ((acc << l) << 1) //guess * guess
      if (guess2 <= v) {
        acc = guess
        acc2 = guess2
      }
    }
    acc.toInt
  }

  def sqrtArray(w: Int = 16, v: Long): Int = {
    var acc = new Array[Long](w + 1)
    var acc2 = new Array[Long](w + 1)
    acc(0) = 0L
    acc2(0) = 0L
    for (l <- (w - 1) to 0 by -1) {
      val i = w - l
      val b = 1L << l
      val b2 = 1L << (l << 1)
      val guess = acc(i - 1) | b
      val guess2 = acc2(i - 1) + b2 + ((acc(i - 1) << l) << 1) //guess * guess
      if (guess2 <= v) {
        acc(i) = guess
        acc2(i) = guess2
      } else {
        acc(i) = acc(i - 1)
        acc2(i) = acc2(i - 1)
      }
    }
    acc(w).toInt
  }

  def sqrtRec(w: Int = 16, v: Long): Int = {
    def loop(i: Int, acc: Long, acc2: Long): Int = {
      if (i == 0)
        return acc.toInt
      val l = i - 1
      val b = 1L << l
      val b2 = 1L << (l << 1)
      val guess = acc | b
      val guess2 = acc2 + b2 + ((acc << l) << 1) //guess * guess
      if (guess2 <= v)
        loop(l, guess, guess2)
      else
        loop(l, acc, acc2)
    }

    loop(w, acc = 0, acc2 = 0)
  }

}

object SqrtFNTest {

  def main(args: Array[String]) {
    val inputs = Seq(0, 1, 2, 3, 4, 25, 27, 254, 255)

    for (v <- inputs) {
      val r = SqrtFN.sqrtArray(4, v)
      println(s"value=$v, result=$r")
      assert(r * r <= v && (r + 1) * (r + 1) >= v)
    }
  }
}