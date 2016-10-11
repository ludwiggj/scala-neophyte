package neophyte.ch14.actors

import akka.actor.Actor
import neophyte.ch14.actors.Barista.{Bill, CappuccinoRequest, ClosingTime, EspressoRequest}

object Barista {

  sealed trait CoffeeRequest

  case object CappuccinoRequest extends CoffeeRequest

  case object EspressoRequest extends CoffeeRequest

  case class Bill(cents: Int)

  case object ClosingTime

}

class Barista extends Actor {
  var cappuccinoCount = 0
  var espressoCount = 0

  def receive = {
    case CappuccinoRequest =>
      sender ! Bill(250)
      cappuccinoCount += 1
      println(s"I have to prepare cappuccino #$cappuccinoCount!")

    case EspressoRequest =>
      sender ! Bill(200)
      espressoCount += 1
      println(s"Let's prepare espresso #$espressoCount.")

    case ClosingTime => context.system.shutdown()
  }
}