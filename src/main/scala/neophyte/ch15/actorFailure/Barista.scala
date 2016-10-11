package neophyte.ch15.actorFailure

import akka.actor.{Actor, Props}
import neophyte.ch15.actorFailure.Barista.EspressoCup.Filled
import neophyte.ch15.actorFailure.Barista.{ClosingTime, EspressoCup, EspressoRequest}
import neophyte.ch15.actorFailure.Register.{Espresso, Transaction}

object Barista {
  case object EspressoRequest
  case object ClosingTime
  case class EspressoCup(state: EspressoCup.State)

  object EspressoCup {
    sealed trait State
    case object Clean extends State
    case object Filled extends State
    case object Dirty extends State
  }
  case class Receipt(amount: Int)
}

class Barista extends Actor {
  import akka.util.Timeout
  import concurrent.duration._
  import akka.pattern.ask
  import akka.pattern.pipe

  implicit val timeout = Timeout(4.seconds)
  implicit val ec = context.dispatcher

  val register = context.actorOf(Props[Register], "Register")

  def receive: Receive = {
    case EspressoRequest =>
      println("Received EspressoRequest")
      val receipt = register ? Transaction(Espresso)
      receipt.map((EspressoCup(Filled), _)).pipeTo(sender)

    case ClosingTime => context.stop(self)
  }
}
