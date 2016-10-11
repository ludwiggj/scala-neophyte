package neophyte.ch15.actorFailure

import akka.actor.{ActorRef, ActorSystem, Props}
import neophyte.ch15.actorFailure.Barista.ClosingTime
import neophyte.ch15.actorFailure.Customer.CaffeineWithdrawalWarning

object CoffeeShop extends App {
  val system = ActorSystem("CoffeeShop")

  val barista: ActorRef = system.actorOf(Props[Barista], "Barista")
  val customerJohnny: ActorRef = system.actorOf(Props(classOf[Customer], barista), "Johnny")
  val customerAlicia: ActorRef = system.actorOf(Props(classOf[Customer], barista), "Alicia")

  println("Let's serve coffee!")

  customerJohnny ! CaffeineWithdrawalWarning
  customerAlicia ! CaffeineWithdrawalWarning

  barista ! ClosingTime
}