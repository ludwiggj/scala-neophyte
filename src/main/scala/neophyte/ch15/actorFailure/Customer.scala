package neophyte.ch15.actorFailure

import akka.actor.{Actor, ActorLogging, ActorRef}
import neophyte.ch15.actorFailure.Barista.EspressoCup.Filled
import neophyte.ch15.actorFailure.Barista.{ComebackLater, EspressoCup, EspressoRequest, Receipt}
import neophyte.ch15.actorFailure.Customer.CaffeineWithdrawalWarning

object Customer {
  case object CaffeineWithdrawalWarning
}

class Customer(caffeineSource: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = {
    case CaffeineWithdrawalWarning =>
      log.info("I need caffeine!")
      caffeineSource ! EspressoRequest

    case (EspressoCup(Filled), Receipt(amount)) =>
      log.info(s"yay, caffeine, as long as I can pay $amount!")

    case ComebackLater =>
      log.info(s"Argh! I neeeeed caffeine!")
  }
}