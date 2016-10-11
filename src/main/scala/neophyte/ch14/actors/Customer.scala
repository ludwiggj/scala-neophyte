package neophyte.ch14.actors

import akka.actor.{Actor, ActorRef}
import neophyte.ch14.actors.Barista.{Bill, EspressoRequest}
import neophyte.ch14.actors.Customer.CaffeineWithdrawalWarning

object Customer {
  case object CaffeineWithdrawalWarning
}

class Customer(caffeineSource: ActorRef) extends Actor {
  def receive: Receive = {
    case CaffeineWithdrawalWarning => caffeineSource ! EspressoRequest
    case Bill(cents) => println(s"I have to pay $cents cents, or else!")
  }
}