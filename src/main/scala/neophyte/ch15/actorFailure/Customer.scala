package neophyte.ch15.actorFailure

import akka.actor.{Actor, ActorLogging, ActorRef, Terminated}
import neophyte.ch15.actorFailure.Barista.EspressoCup.Filled
import neophyte.ch15.actorFailure.Barista.{ComebackLater, EspressoCup, EspressoRequest, Receipt}
import neophyte.ch15.actorFailure.Customer.CaffeineWithdrawalWarning

import concurrent.duration._

object Customer {
  case object CaffeineWithdrawalWarning
}

class Customer(caffeineSource: ActorRef) extends Actor with ActorLogging {
  import context.dispatcher
  context.watch(caffeineSource)

  def receive: Receive = {
    case CaffeineWithdrawalWarning =>
      log.info("I need caffeine!")
      caffeineSource ! EspressoRequest

    case (EspressoCup(Filled), Receipt(amount)) =>
      log.info(s"yay, caffeine, as long as I can pay $amount!")

    case ComebackLater =>
      log.info(s"Argh! I neeeeed caffeine!")

      log.info("Let's try one more time for that cup a joe")
      context.system.scheduler.scheduleOnce(300.millis) {
        caffeineSource ! EspressoRequest
      }

    case Terminated(barista) =>
      log.info(s"Oh well, $barista is no longer serving. Let's find another coffee shop")
  }
}