package neophyte.promises

import scala.concurrent.{Await, Promise, Future}
import scala.util.{Try, Success, Failure}
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Government extends App {

  type Redeemer = (Promise[TaxCut]) => Unit

  def redeemCampaignPledge(redeem: Redeemer): Future[TaxCut] = {
    val p = Promise[TaxCut]()

    Future {
      println("Starting a new term of office...")
      Thread.sleep(2000)
      redeem(p)
    }

    p.future
  }

  def makeATaxCut(promise: Promise[TaxCut]) {
    promise.success(TaxCut(10))
    println("taxes are reduced... huzzah!")
  }

  def makeADeeperTaxCut(promise: Promise[TaxCut]) {
    promise.complete(Try { TaxCut(20) } )
    println("taxes are reduced... huzzah!")
  }

  case class LameExcuse(msg: String) extends Exception(msg)

  def breakAPromise(promise: Promise[TaxCut]) {
    promise.failure(LameExcuse("global economic crisis"))
  }

  def breakAnotherPromise(promise: Promise[TaxCut]) {
    promise.complete(Try { throw new LameExcuse("global warming, glug!") })
  }

  def checkOnGovernment(redeem: Redeemer): Unit = {
    Await.ready(Government.redeemCampaignPledge(redeem), 10.seconds).value.get match {
      case Success(TaxCut(reduction)) =>
        println(s"Yay! They cut our taxes by $reduction%")
      case Failure(ex) =>
        println(s"Broken promise, because ${ex.getMessage}")
    }
  }

  println("Let's see if they remember their promises")

  checkOnGovernment(makeATaxCut)

  checkOnGovernment(makeADeeperTaxCut)

  println("What about this time?")

  checkOnGovernment(breakAPromise)
  
  checkOnGovernment(breakAnotherPromise)
}