package shs.btsort

import spinal.core.isPow2

// A von Neumann algorithm!

object BitonicSortVN {

  def swap(up: Boolean, x: Array[Int], i: Int, j: Int): Unit = {
    if ((x(i) > x(j)) == up) {
      val tmp = x(i)
      x(i) = x(j)
      x(j) = tmp
    }
  }

  def merge(up: Boolean, x: Array[Int]): Array[Int] = {
    // Produces a sorted list, on condition that x is bitonic.
    val l = x.length
    if (l == 1)
      return x
    val h = l / 2
    for (i <- 0 until h)
      swap(up, x, i, i + h)
    merge(up, x.take(h)) ++ merge(up, x.drop(h))
  }

  def sort(up: Boolean, x: Array[Int]): Array[Int] = {
    val l = x.length
    require(isPow2(l), "x.length must be a power of 2")
    if (l <= 1)
      return x
    val h = l / 2
    val x1 = sort(up = true, x = x.take(h))
    val x2 = sort(up = false, x = x.drop(h))
    merge(up, x1 ++ x2)
  }
}

object BitonicSortVNTest {
  def main(args: Array[String]): Unit = {
    val input = Vector(10, 30, 11, 20, 4, 330, 21, 110)
    val x = input.toArray
    println(s"inputs = $input")
    val result = BitonicSortVN.sort(up = true, input.toArray).toVector
    println(s"outputs = $result")
    require(input.sorted == result)
  }
}
