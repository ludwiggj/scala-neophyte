package neophyte.ch15.actorFailure

//import akka.actor.SupervisorStrategy.{Directive, Resume}
//import akka.actor.{OneForOneStrategy, SupervisorStrategy}
import akka.actor.{Actor, Props}
import akka.pattern.AskTimeoutException
import neophyte.ch15.actorFailure.Barista.EspressoCup.Filled
import neophyte.ch15.actorFailure.Barista.{ClosingTime, ComebackLater, EspressoCup, EspressoRequest}
import neophyte.ch15.actorFailure.Register.{Espresso, Transaction}

object Barista {

  case object EspressoRequest

  case object ClosingTime

  case object ComebackLater

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

  implicit val timeout = Timeout(2.seconds)
  implicit val ec = context.dispatcher

  // Supervision strategy to resume Register actor on a paper jam
  //  val decider: PartialFunction[Throwable, Directive] = {
  //    case _: PaperJamException => Resume
  //  }
  //
  //  override def supervisorStrategy: SupervisorStrategy =
  //    OneForOneStrategy()(decider.orElse(SupervisorStrategy.defaultStrategy.decider))

  //  val register = context.actorOf(Props[Register], "Register")

  val register = context.actorOf(Props[ResilientRegister], "Register")

  def receive: Receive = {
    case EspressoRequest =>
      println("Received EspressoRequest")

      val receipt = register ? Transaction(Espresso)

      receipt.map((EspressoCup(Filled), _)).recover {
        case _: AskTimeoutException => ComebackLater
      } pipeTo (sender)

    case ClosingTime => context.stop(self)
  }
}