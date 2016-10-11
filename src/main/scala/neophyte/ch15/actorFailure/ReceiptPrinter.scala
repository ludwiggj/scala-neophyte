package neophyte.ch15.actorFailure

import akka.actor.{Actor, ActorLogging}
import neophyte.ch15.actorFailure.Barista.Receipt
import neophyte.ch15.actorFailure.ReceiptPrinter.PrintJob
import util.Random

object ReceiptPrinter {

  case class PrintJob(amount: Int)

}

class ReceiptPrinter extends Actor with ActorLogging {

  var paperJam = false

  override def postRestart(reason: Throwable) {
    super.postRestart(reason)
    log.info(s"Restarted, paper jam == $paperJam")
  }

  def receive = {
    case PrintJob(amount) =>
      sender ! createReceipt(amount)
  }

  def createReceipt(price: Int): Receipt = {

    if (Random.nextBoolean()) paperJam = true
    if (paperJam) throw new PaperJamException("OMG, not again!")
    Receipt(price)
  }
}