package neophyte.ch09.promises

import java.util.concurrent.TimeoutException

import scala.language.postfixOps
import scala.concurrent.{Promise, Await, Future}
import scala.concurrent.duration._
import concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object WaitOnMultipleFuturesInParallel {

  def getFuturesInOrderOfCompletion(recorder: StringBuilder) = {
    val future1 = Future {
      Thread.sleep(3000);
      recorder.append("Future 1 about to complete after 3 seconds with value 2\n")
      2
    }

    val future2 = Promise.failed(new scala.RuntimeException("Future 2 has failed... boo!")).future

    val future3 = Future {
      Thread.sleep(1000);
      recorder.append("Future 3 about to complete after 1 second with value 3\n")
      3
    }

    (future3, future2, future1)
  }

  def getListOfFuturesInOrderOfCompletion(recorder: StringBuilder) = {
    getFuturesInOrderOfCompletion(recorder).productIterator.toList.asInstanceOf[List[Future[Any]]]
  }

  def getListOfFuturesInReverseOrderOfCompletion(recorder: StringBuilder) = {
    getListOfFuturesInOrderOfCompletion(recorder).reverse
  }

  def getFuturesInReverseOrderOfCompletion(recorder: StringBuilder) = {
    val futures = getFuturesInOrderOfCompletion(recorder)

    (futures._3, futures._2, futures._1)
  }

  def composeWaitingFuture[T](fut: Future[T], atMost: FiniteDuration, default: T) =
    Future {
      Await.result(fut, atMost)
    } recover {
      case e: Exception => default
    }

  def getComposedWaitingFutures[T](futures: (Future[T], Future[T], Future[T]), atMost: FiniteDuration) = {
    val future1 = composeWaitingFuture(futures._1, atMost, Map[String, Int]())
    val future2 = composeWaitingFuture(futures._2, atMost, List[Int]())
    val future3 = composeWaitingFuture(futures._3, atMost, Map[String, BigInt]())

    (future1, future2, future3)
  }

  def combineFutures[T](futures: (Future[T], Future[T], Future[T])) = {
    // takes the maximum of max(timeout1, timeout2, timeout3) to complete
    for {
      r1 <- futures._1
      r2 <- futures._2
      r3 <- futures._3
    } yield (r1, r2, r3)
  }

  def waitOnFuture[T](future: Future[T], atMost: FiniteDuration, recorder: StringBuilder) = {
    try {
      Await.ready(future, atMost).value.get match {
        case Success(result) =>
          recorder.append(s"A result... $result")
        case Failure(ex) =>
          recorder.append(s"Oh dear... ${ex.getMessage}")
      }
    } catch {
      case ex: TimeoutException =>
        recorder.append(ex.getMessage)
    }
  }
}