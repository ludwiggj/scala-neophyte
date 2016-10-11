package neophyte.ch15.actorFailure

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.{ask, pipe}

import concurrent.duration._
import akka.util.Timeout
import neophyte.ch15.actorFailure.Barista.Receipt
import neophyte.ch15.actorFailure.ReceiptPrinter.PrintJob
import neophyte.ch15.actorFailure.Register.{Cappuccino, Espresso, Item, Transaction}

class ResilientRegister extends Actor with ActorLogging {
  implicit val timeout = Timeout(2.seconds)
  var revenue = 0
  val prices = Map[Item, Int](Espresso -> 150, Cappuccino -> 250)
  val printer = context.actorOf(Props[ReceiptPrinter], "Printer")
  implicit val ec = context.dispatcher

  def receive = {
    case Transaction(item) =>
      val price = prices(item)
      val requester = sender
      (printer ? PrintJob(price)).map((requester, _)).pipeTo(self)

    case (requester: ActorRef, receipt: Receipt) =>
      revenue += receipt.amount
      log.info(s"Revenue incremented to $revenue cents")
      requester ! receipt
  }
}