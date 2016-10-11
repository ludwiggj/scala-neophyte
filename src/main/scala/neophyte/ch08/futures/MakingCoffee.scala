package neophyte.ch08.futures

import neophyte.ch08.futures.CoffeeFuture._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object MakingCoffee extends App {

  def prepareCappuccinoSequentially(): Future[Cappuccino] = for {
    ground <- grind("arabica beans")
    water <- heatWater(Water(20))
    foam <- frothMilk("milk")
    espresso <- brew(ground, water)
  } yield combine(espresso, foam)

  Await.ready(prepareCappuccinoSequentially(), 10.seconds).value.get match {
    case Success(drink) => println(s"One drink ready.... ${drink}")
  }

  def prepareFastCappuccino(): Future[Cappuccino] = {
    val groundCoffee: Future[GroundCoffee] = grind("arabica beans")
    val heatedWater: Future[Water] = heatWater(Water(20))
    val frothedMilk: Future[FrothedMilk] = frothMilk("milk")

    for {
      ground <- groundCoffee
      water <- heatedWater
      foam <- frothedMilk
      espresso <- brew(ground, water)
    } yield combine(espresso, foam)
  }

  Await.ready(prepareFastCappuccino(), 10.seconds).value.get match {
    case Success(drink) => println(s"One drink ready.... ${drink} ... hope that was faster madam!")
  }

  prepareCappuccinoSequentially().failed

  println("Fin")
}