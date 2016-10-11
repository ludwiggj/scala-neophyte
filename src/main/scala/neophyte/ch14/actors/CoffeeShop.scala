package neophyte.ch14.actors

import akka.actor.{ActorRef, ActorSystem, Props}
import neophyte.ch14.actors.Barista.{Bill, CappuccinoRequest, ClosingTime}
import neophyte.ch14.actors.Customer.CaffeineWithdrawalWarning

import scala.concurrent.Future

object CoffeeShop extends App {
  val system = ActorSystem("CoffeeShop")

  val barista: ActorRef = system.actorOf(Props[Barista], "Barista")
  val customer: ActorRef = system.actorOf(Props(classOf[Customer], barista), "Customer")

  customer ! CaffeineWithdrawalWarning
  barista ! CappuccinoRequest
  println("I ordered a cappuccino and an espresso")

  // Let's ask for a Cappucino
  import akka.pattern.ask
  import akka.util.Timeout

  import scala.concurrent.duration._

  implicit val timeout = Timeout(2.second)
  implicit val ec = system.dispatcher

  val f: Future[Any] = barista ? CappuccinoRequest
  f.onSuccess {
    case Bill(cents) => println(s"Will pay $cents cents for a cappuccino")
  }

  barista ! ClosingTime
}
