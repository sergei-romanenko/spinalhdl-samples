package shs.btsort_f

import spinal.core.isPow2

// A von Neumann algorithm!

object BitonicSortVN {

  def merge(up: Boolean, x: Vector[Int]): Vector[Int] = {
    // Produces a sorted list, on condition that x is bitonic.
    val l = x.length
    if (l == 1)
      return x
    val h = l / 2
    val (x1, x2) = x.splitAt(h)
    val ok = Vector.tabulate(h)(i => if (up) x1(i) <= x2(i) else x2(i) <= x1(i))
    val y1 = Vector.tabulate(h)(i => if (ok(i)) x1(i) else x2(i))
    val y2 = Vector.tabulate(h)(i => if (ok(i)) x2(i) else x1(i))
    merge(up, y1) ++ merge(up, y2)
  }

  def sort(up: Boolean, x: Vector[Int]): Vector[Int] = {
    val l = x.length
    require(isPow2(x.length), "x.length must be a power of 2")
    if (l <= 1)
      return x
    val h = l / 2
    val (x1, x2) = x.splitAt(h)
    val y1 = sort(up = true, x1)
    val y2 = sort(up = false, x2)
    merge(up, y1 ++ y2)
  }
}

object BitonicSortVNTest {
  def main(args: Array[String]): Unit = {
    val input = Vector(10, 30, 11, 20, 4, 330, 21, 110)
    println(s"inputs = $input")
    val result = BitonicSortVN.sort(up = true, input)
    println(s"outputs = $result")
    require(input.sorted == result, "input.sorted == result")
  }
}
