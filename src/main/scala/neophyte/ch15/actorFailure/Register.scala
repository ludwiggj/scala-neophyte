package neophyte.ch15.actorFailure

import akka.actor._

object Register {

  sealed trait Item

  case object Espresso extends Item

  case object Cappuccino extends Item

  case class Transaction(item: Item)

}

class Register extends Actor with ActorLogging {

  import Register._
  import Barista._

  var revenue = 0
  val prices = Map[Item, Int](Espresso -> 150, Cappuccino -> 250)

  def receive = {
    case Transaction(item) =>
      val price = prices(item)
      sender ! createReceipt(price)
      revenue += price
      log.info(s"Revenue incremented to $revenue cents")
  }

  def createReceipt(price: Int): Receipt = {
    import util.Random
    if (Random.nextBoolean())
      throw new PaperJamException("OMG, not again!")
    Receipt(price)
  }

  override def postRestart(reason: Throwable) {
    super.postRestart(reason)
    log.info(s"Restarted: revenue is $revenue cents. Cause: ${reason.getMessage}")
  }
}