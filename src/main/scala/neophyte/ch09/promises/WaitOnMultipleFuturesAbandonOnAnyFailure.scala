package neophyte.ch09.promises

object WaitOnMultipleFuturesAbandonOnAnyFailure extends App {

  import concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.{Future, Promise}
  import scala.util.{Failure, Success}

  val fut1 = Future {
    Thread.sleep(3000);
    1
  }
  val fut2 = Promise.failed(new RuntimeException("boo")).future
  val fut3 = Future {
    Thread.sleep(1000);
    3
  }

  def processFutures(futures: Map[Int, Future[Int]], values: List[Any], prom: Promise[List[Any]]): Future[List[Any]] = {
    val fut = if (futures.size == 1) futures.head._2
    else Future.firstCompletedOf(futures.values)

    fut onComplete {
      case Success(value) if (futures.size == 1) =>
        prom.success(value :: values)

      case Success(value) =>
        processFutures(futures - value, value :: values, prom)

      case Failure(ex) => prom.failure(ex)
    }
    prom.future
  }

  val aggFut = processFutures(Map(1 -> fut1, 2 -> fut2, 3 -> fut3), List(), Promise[List[Any]]())
  aggFut onComplete {
    case value => println(value)
  }
}