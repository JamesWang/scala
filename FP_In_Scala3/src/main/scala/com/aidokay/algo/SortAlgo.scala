package com.aidokay.algo

import scala.annotation.tailrec

object SortAlgo {
  def swap(arr: Array[Int], one: Int, other: Int): Unit =
    if (one < arr.length && other < arr.length) {
      val temp = arr(one)
      arr(one) = arr(other)
      arr(other) = temp
    }

  //everything before prevIndex are sorted for each while loop
  def insertionSort(arr: Array[Int]): Unit =
    if (arr.length > 1) {
      for (i <- 1 until arr.length) {
        val current = arr(i)
        var prevIndex = i - 1
        while (prevIndex >= 0 && current < arr(prevIndex)) {
          arr(prevIndex + 1) = arr(prevIndex)
          prevIndex = prevIndex - 1
        }
        arr(prevIndex + 1) = current
      }
    }

  def bubbleSort(ints: Array[Int]): Unit =
    var sorted = false
    while (!sorted) {
      sorted = true
      for (i <- 0 until ints.length - 1) {
        if (ints(i) > ints(i + 1)) {
          swap(ints, i, i + 1)
          sorted = false
        }
      }
    }

  def selectionSort(arr: Array[Int]): Unit =
    if (arr.length > 1) {
      for (i <- arr.indices) {
        var min = arr(i)
        var minIdx = i;
        for (j <- i until arr.length) {
          if (arr(j) < min) {
            min = arr(j)
            minIdx = j
          }
        }
        swap(arr, i, minIdx)
      }
    }

  def quickSort(xs: Array[Int]): Array[Int] =
    if (xs.length <= 1) xs
    else {
      val pivot = xs(xs.length / 2)
      Array.concat(
        quickSort(xs.filter(pivot > _)),
        xs.filter(_ == pivot),
        quickSort(xs.filter(pivot < _))
      )
    }

  def quickSortL(xs: List[Int]): List[Int] =
    if (xs.size <= 1) xs
    else {
      val pivot = xs(xs.size / 2)
      quickSortL(xs.filter(pivot > _))
        :::
        xs.filter(_ == pivot)
        :::
        quickSortL(xs.filter(pivot < _))

    }

  def main(args: Array[String]): Unit = {
    val arr = Array(5, 2, 6, 1, 3, 4)
    //println(quickSortL(arr.toList))
    //bubbleSort(arr)
    //println(arr.mkString("Array(", ", ", ")"))
    println(insertionSort(arr))

  }
}
