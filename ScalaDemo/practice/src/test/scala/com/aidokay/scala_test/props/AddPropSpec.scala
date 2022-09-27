package com.aidokay.scala_test.props

import com.aidokay.scala_test.Calculator
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor3}
import org.scalatest.propspec.AnyPropSpec

import scala.reflect.runtime.universe._

class AddPropSpec extends AnyPropSpec with TableDrivenPropertyChecks with Matchers {

  def runTest[T](testData:TableFor3[T,T,T])(implicit calculator: Calculator[T], tag: TypeTag[T]) : Unit = {
    //val t = typeOf[T]
    //println(tag)
    property(s"Addition of two numbers of ${tag.tpe}"){
      forAll(testData){
        (a,b,result) ⇒ calculator.add(a,b) should be(result)
      }
    }
  }
  import com.aidokay.scala_test.CalculatorInstances._
  val testData1: TableFor3[Int, Int, Int] = Table(
    ("a","b", "result"),
    (3,3,6),
    (2,42, 44)
  )
  runTest(testData1)

  val testData2: TableFor3[Double, Double, Double] = Table(
    ("a","b", "result"),
    (3.3,4.4,7.7),
    (0.002,0.112, 0.114)
  )
  runTest(testData2)

  //(BigDecimal("234.21"),BigDecimal(123.12), BigDecimal(357.33))
}
