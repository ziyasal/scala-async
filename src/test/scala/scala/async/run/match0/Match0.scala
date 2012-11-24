/*
 * Copyright (C) 2012 Typesafe Inc. <http://www.typesafe.com>
 */

package scala.async
package run
package match0

import language.{reflectiveCalls, postfixOps}
import scala.concurrent.{Future, ExecutionContext, future, Await}
import scala.concurrent.duration._
import scala.async.Async.{async, await}
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Test


class TestMatchClass {

  import ExecutionContext.Implicits.global

  def m1(x: Int): Future[Int] = future {
    x + 2
  }

  def m2(y: Int): Future[Int] = async {
    val f = m1(y)
    var z = 0
    y match {
      case 10 =>
        val x1 = await(f)
        z = x1 + 2
      case 20 =>
        val x2 = await(f)
        z = x2 - 2
    }
    z
  }

  def m3(y: Int): Future[Int] = async {
    val f = m1(y)
    var z = 0
    y match {
      case 0 =>
        val x2 = await(f)
        z = x2 - 2
      case 1 =>
        val x1 = await(f)
        z = x1 + 2
    }
    z
  }
}


@RunWith(classOf[JUnit4])
class MatchSpec {

  @Test def `support await in a simple match expression`() {
    val o = new TestMatchClass
    val fut = o.m2(10) // matches first case
    val res = Await.result(fut, 2 seconds)
    res mustBe (14)
  }

  @Test def `support await in a simple match expression 2`() {
    val o = new TestMatchClass
    val fut = o.m3(1) // matches second case
    val res = Await.result(fut, 2 seconds)
    res mustBe (5)
  }
}
