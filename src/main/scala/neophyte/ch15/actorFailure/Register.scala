package neophyte.ch15.actorFailure

import akka.actor._

object Register {

  sealed trait Item

  case object Espresso extends Item

  case object Cappuccino extends Item

  case class Transaction(item: Item)

}

class Register extends Actor {

  import Register._
  import Barista._

  var revenue = 0
  val prices = Map[Item, Int](Espresso -> 150, Cappuccino -> 250)

  def receive = {
    case Transaction(item) =>
      val price = prices(item)
      sender ! createReceipt(price)
      revenue += price
  }

  def createReceipt(price: Int): Receipt = Receipt(price)
}