package shs.gcd

// A von Neumann algorithm!

object GCDFN {

  def gcdRecRem(a: Long, b: Long): Long = {
    if (b == 0) a else gcdRecRem(b, a % b)
  }

  def gcdRec(a: Long, b: Long): Long = {
    if (a == 0)
      b
    else if (b == 0)
      a
    else if (a >= b)
      gcdRec(a - b, b)
    else
      gcdRec(a, b - a)
  }

  def gcdIter(a: Long, b: Long): Long = {
    var x = a
    var y = b
    while (y != 0) {
      while (x >= y)
        x = x - y
      var tmp = x
      x = y
      y = tmp
    }
    x
  }
}

object GCDFNTest {

  def main(args: Array[String]) {
    val inputs = Seq(
      (0, 0, 0),
      (0, 5, 5),
      (5, 0, 5),
      (21, 49, 7),
      (25, 30, 5),
      (19, 27, 1),
      (40, 40, 40),
      (250, 190, 10),
      (5, 250, 5))

    for ((a, b, c) <- inputs) {
      val r = GCDFN.gcdRec(a, b)
      println(s"a=$a, b=$b, r=$r")
      assert(c == r)
    }
  }
}