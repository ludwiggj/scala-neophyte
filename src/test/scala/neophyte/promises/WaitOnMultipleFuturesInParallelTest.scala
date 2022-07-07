package neophyte.promises

import neophyte.ch09.promises.WaitOnMultipleFuturesInParallel._
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.Future
import scala.concurrent.duration._
import concurrent.ExecutionContext.Implicits.global

import scala.language.postfixOps

class WaitOnMultipleFuturesInParallelTest extends FunSuite with Matchers {

  test("ComposedWaitingFutures in order of completion with a future timing out") {
    val result = new StringBuilder()

    waitOnFuture(
      combineFutures(getComposedWaitingFutures(getFuturesInOrderOfCompletion(result), 2 seconds)),
      3 seconds,
      result
    )

    result.toString() should equal(
      "Future 3 about to complete after 1 second with value 3\n" +
        "A result... (3,List(),Map())"
    )
  }

  test("List of ComposedWaitingFutures in order of completion with a future timing out") {
    val result = new StringBuilder()

    waitOnFuture(
      Future.sequence(getListOfFuturesInOrderOfCompletion(result).map {
        fut => composeWaitingFuture(fut, 2 seconds, Map[String, Int]())
      }),
      3 seconds,
      result
    )

    result.toString() should equal(
      "Future 3 about to complete after 1 second with value 3\n" +
        "A result... List(3, Map(), Map())"
    )
  }

  test("ComposedWaitingFutures in reverse order of completion with a future timing out") {
    val result = new StringBuilder()

    waitOnFuture(
      combineFutures(getComposedWaitingFutures(getFuturesInReverseOrderOfCompletion(result), 2 seconds)),
      3 seconds,
      result
    )

    result.toString() should equal(
      "Future 3 about to complete after 1 second with value 3\n" +
        "A result... (Map(),List(),3)"
    )
  }

  test("List of ComposedWaitingFutures in reverse order of completion with a future timing out") {
    val result = new StringBuilder()

    waitOnFuture(
      Future.sequence(getListOfFuturesInReverseOrderOfCompletion(result).map {
        fut => composeWaitingFuture(fut, 2 seconds, Map[String, Int]())
      }),
      3 seconds,
      result
    )

    result.toString() should equal(
      "Future 3 about to complete after 1 second with value 3\n" +
        "A result... List(Map(), Map(), 3)"
    )
  }

  test("ComposedWaitingFutures in order of completion with all futures completing") {
    val result = new StringBuilder()

    waitOnFuture(
      combineFutures(getComposedWaitingFutures(getFuturesInOrderOfCompletion(result), 3 seconds)),
      4 seconds,
      result
    )

    result.toString() should equal(
      "Future 3 about to complete after 1 second with value 3\n" +
        "Future 1 about to complete after 3 seconds with value 2\n" +
        "A result... (3,List(),2)"
    )
  }

  test("List of ComposedWaitingFutures in order of completion with all futures completing") {
    val result = new StringBuilder()

    waitOnFuture(
      Future.sequence(getListOfFuturesInOrderOfCompletion(result).map {
        fut => composeWaitingFuture(fut, 3 seconds, Map[String, Int]())
      }),
      4 seconds,
      result
    )

    result.toString() should equal(
      "Future 3 about to complete after 1 second with value 3\n" +
        "Future 1 about to complete after 3 seconds with value 2\n" +
        "A result... List(3, Map(), 2)"
    )
  }

  test("ComposedWaitingFutures in reverse order of completion with all futures timing out") {
    val result = new StringBuilder()

    waitOnFuture(
      combineFutures(getComposedWaitingFutures(getFuturesInReverseOrderOfCompletion(result), 0.5 seconds)),
      1 second,
      result
    )

    result.toString() should equal(
      "A result... (Map(),List(),Map())"
    )
  }

  test("List of ComposedWaitingFutures in reverse order of completion with all futures timing out") {
    val result = new StringBuilder()

    waitOnFuture(
      Future.sequence(getListOfFuturesInReverseOrderOfCompletion(result).map {
        fut => composeWaitingFuture(fut, 20 milliseconds, Map[String, Int]())
      }),
      1 second,
      result
    )

    result.toString() should equal(
      "A result... List(Map(), Map(), Map())"
    )
  }

  test("Futures in order of completion with a future timing out") {
    val result = new StringBuilder()

    waitOnFuture(
      combineFutures(getFuturesInOrderOfCompletion(result)),
      4 seconds,
      result
    )

    result.toString() should equal(
      "Future 3 about to complete after 1 second with value 3\n" +
        "Oh dear... Future 2 has failed... boo!"
    )
  }

  test("List of futures in order of completion with a future timing out") {
    val result = new StringBuilder()

    waitOnFuture(
      Future.sequence(getListOfFuturesInOrderOfCompletion(result)),
      4 seconds,
      result
    )

    result.toString() should equal(
      "Future 3 about to complete after 1 second with value 3\n" +
        "Oh dear... Future 2 has failed... boo!"
    )
  }

  test("Futures in reverse order of completion with a future timing out") {
    val result = new StringBuilder()

    waitOnFuture(
      combineFutures(getFuturesInReverseOrderOfCompletion(result)),
      4 seconds,
      result
    )

    result.toString() should equal(
      "Future 3 about to complete after 1 second with value 3\n" +
        "Future 1 about to complete after 3 seconds with value 2\n" +
        "Oh dear... Future 2 has failed... boo!"
    )
  }

  test("List of futures in reverse order of completion with a future timing out") {
    val result = new StringBuilder()

    waitOnFuture(
      Future.sequence(getListOfFuturesInReverseOrderOfCompletion(result)),
      4 seconds,
      result
    )

    result.toString() should equal(
      "Future 3 about to complete after 1 second with value 3\n" +
        "Future 1 about to complete after 3 seconds with value 2\n" +
        "Oh dear... Future 2 has failed... boo!"
    )
  }

  test("ComposedWaitingFutures in reverse order of completion time out on combined future") {
    val result = new StringBuilder()

    waitOnFuture(
      combineFutures(getComposedWaitingFutures(getFuturesInReverseOrderOfCompletion(result), 3 seconds)),
      1 second,
      result
    )

    result.toString() should equal(
      "Future 3 about to complete after 1 second with value 3\n" +
        "Futures timed out after [1 second]"
    )
  }

  test("List of ComposedWaitingFutures in reverse order of completion time out on combined future") {
    val result = new StringBuilder()

    waitOnFuture(
      Future.sequence(getListOfFuturesInReverseOrderOfCompletion(result).map {
        fut => composeWaitingFuture(fut, 0.5 seconds, Map[String, Int]())
      }),
      1 second,
      result
    )

    result.toString() should equal(
      "Future 3 about to complete after 1 second with value 3\n" +
        "Futures timed out after [1 second]"
    )
  }

  test("Futures in reverse order of completion time out on combined future") {
    val result = new StringBuilder()

    waitOnFuture(
      combineFutures(getFuturesInReverseOrderOfCompletion(result)),
      0.5 seconds,
      result
    )

    result.toString() should equal(
      "Futures timed out after [500 milliseconds]"
    )
  }

  test("List of futures in reverse order of completion time out on combined future") {
    val result = new StringBuilder()

    waitOnFuture(
      Future.sequence(getListOfFuturesInReverseOrderOfCompletion(result)),
      0.5 seconds,
      result
    )

    result.toString() should equal(
      "Futures timed out after [500 milliseconds]"
    )
  }
}