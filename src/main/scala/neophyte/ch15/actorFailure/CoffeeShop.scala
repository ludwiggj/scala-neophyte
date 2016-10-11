package neophyte.ch15.actorFailure

import akka.actor.{ActorRef, ActorSystem, Props}
import neophyte.ch15.actorFailure.Barista.ClosingTime
import neophyte.ch15.actorFailure.Customer.CaffeineWithdrawalWarning

object CoffeeShop extends App {
  val system = ActorSystem("CoffeeShop")

  val barista: ActorRef = system.actorOf(Props[Barista], "Barista")
  val customerJohnny: ActorRef = system.actorOf(Props(classOf[Customer], barista), "Johnny")
  val customerBert: ActorRef = system.actorOf(Props(classOf[Customer], barista), "Bert")
  val customerErnie: ActorRef = system.actorOf(Props(classOf[Customer], barista), "Ernie")
  val customerMel: ActorRef = system.actorOf(Props(classOf[Customer], barista), "Mel")
  val customerSue: ActorRef = system.actorOf(Props(classOf[Customer], barista), "Sue")
  val customerAlicia: ActorRef = system.actorOf(Props(classOf[Customer], barista), "Alicia")

  println("Let's serve coffee!")

  customerJohnny ! CaffeineWithdrawalWarning
  customerMel ! CaffeineWithdrawalWarning
  customerAlicia ! CaffeineWithdrawalWarning
  customerSue ! CaffeineWithdrawalWarning
  customerBert ! CaffeineWithdrawalWarning
  customerErnie ! CaffeineWithdrawalWarning

  Thread.sleep(3000);

  barista ! ClosingTime

  Thread.sleep(1500);

  system.shutdown()
}