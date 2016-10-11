package neophyte.ch08.futures

import neophyte.ch08.futures.CoffeeFuture._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object HeatingWater extends App {

  def temperatureOkay(heatingProcess: Water => Future[Water]): Future[Boolean] = heatingProcess(Water(25)).map {
    water =>
      println("we're in the future")
      (80 to 85).contains(water.temperature)
  }

  def temperatureOkayExpectingFailure(heatingProcess: Water => Future[Water]): Future[Boolean] =
    heatingProcess(Water(25)).failed.map {
      failure => {
        println(s"we're in the future and it's failed... ${failure.getMessage}")
        false
      }
    }

  def awaitOnWaterTemperature(heatTheWater: Future[Boolean]) = {
    Await.ready(heatTheWater, 10.seconds).value.get match {
      case Success(okay) => println(s"Water at correct temperature? $okay")
      case Failure(e) => println(s"oops, no coffee today! ${e.getClass} ${e.getMessage}")
    }
  }

  println("[temperatureOkay... heatWater]")
  awaitOnWaterTemperature(temperatureOkay(heatWater))

  println("[temperatureOkayExpectingFailure... heatWater]")
  awaitOnWaterTemperature(temperatureOkayExpectingFailure(heatWater))

  println("[temperatureOkay... heatWaterWithDodgyHeater]")
  awaitOnWaterTemperature(temperatureOkay(heatWaterWithDodgyHeater))

  println("[temperatureOkayExpectingFailure... heatWaterWithDodgyHeater]")
  awaitOnWaterTemperature(temperatureOkayExpectingFailure(heatWaterWithDodgyHeater))

  def temperatureOk(water: Water): Future[Boolean] = Future {
    (80 to 85).contains(water.temperature)
  }

  def nestedFuture: Future[Future[Boolean]] = heatWater(Water(25)).map {
    water => temperatureOk(water)
  }

  println("[nestedFuture...]")

  Await.ready(nestedFuture, 10.seconds).value.get match {
    case Success(containedOkay) => containedOkay.value.get match {
      case Success(okay) => println(s"Water at correct temperature? ${okay}")
    }
  }

  def flatFuture: Future[Boolean] = heatWater(Water(25)).flatMap {
    water => temperatureOk(water)
  }

  println("[flatFuture...]")

  Await.ready(flatFuture, 10.seconds).value.get match {
    case Success(okay) => println(s"Water at correct temperature? ${okay}")
  }

  def coffeeComprehension: Future[Boolean] = for {
    heatedWater <- heatWater(Water(25))
    okay <- temperatureOk(heatedWater)
  } yield okay

  println("[coffeeComprehension...]")

  Await.ready(coffeeComprehension, 10.seconds).value.get match {
    case Success(okay) => println(s"Water at correct temperature? ${okay}")
  }

  println("Fin")
}