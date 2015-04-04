package neophyte.futures

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

object CoffeeFuture extends App {

  def grind(beans: CoffeeBeans): Future[GroundCoffee] = Future {
    println("Start grinding now...")
    Thread.sleep(Random.nextInt(2000))
    if (beans == "baked beans") throw GrindingException("are you joking?")
    println("Finished grinding...")
    s"ground coffee of $beans"
  }

  def heatWater(water: Water): Future[Water] = Future {
    println("Heating the water now...")
    Thread.sleep(Random.nextInt(2000))
    println("Hot, it's hot!")
    water.copy(temperature = 85)
  }

  def heatWaterWithDodgyHeater(water: Water): Future[Water] = Future {
    println("Heating the water now...")
    Thread.sleep(Random.nextInt(2000))
    println("Oh no!......")
    throw new WaterBoilingException("The heater exploded...")
  }

  def frothMilk(milk: Milk): Future[FrothedMilk] = Future {
    println("Milk frothing engaged...")
    Thread.sleep(Random.nextInt(2000))
    println("Shutting down milk frothing systems...")
    s"frothed $milk"
  }

  def brew(coffee: GroundCoffee, heatedWater: Water): Future[Espresso] = Future {
    println("Happy brewing...")
    Thread.sleep(Random.nextInt(2000))
    println("Here's your coffee madam...")
    "espresso"
  }

  def combine(espresso: Espresso, frothedMilk: FrothedMilk): Cappuccino = "cappuccino"

  // some exceptions for things that might go wrong in the individual steps
  // (we'll need some of them later, use the others when experimenting
  // with the code):
  case class GrindingException(msg: String) extends scala.Exception(msg)

  case class FrothingException(msg: String) extends scala.Exception(msg)

  case class WaterBoilingException(msg: String) extends scala.Exception(msg)

  case class BrewingException(msg: String) extends scala.Exception(msg)

  def grindBeans(beans: String): Unit = {
    grind(beans).onComplete {
      case Success(ground) => {
        println(s"Got my ground $ground")
      }
    }
  }

  def awaitOnGrindingSuccess(beans: String) = {
    // NOTE: Can't await on grindBeans as it is not Awaitable
    // val coffee: GroundCoffee = Await.result(grindBeans(beans), 10.seconds)
    val coffee: GroundCoffee = Await.result(grind(beans), 10.seconds)
    println(coffee)
  }

  def awaitOnGrinding(beans: String) = {
    val coffee: Future[GroundCoffee] = Await.ready(grind(beans), 10.seconds)

    coffee.value.get match {
      case Success(coffee) => println(s"$coffee is ready...")
      case Failure(e) => println(s"oh dear, ${e.getMessage}")
    }
  }
}