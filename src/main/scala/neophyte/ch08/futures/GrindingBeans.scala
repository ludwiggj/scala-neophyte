package neophyte.ch08.futures

import CoffeeFuture._

object GrindingBeans extends App {

  awaitOnGrinding("ethiopian choice beans")
  awaitOnGrinding("baked beans")

  awaitOnGrindingSuccess("arabica beans")

  grindBeans("machu pichu")
  Thread.sleep(2000)

  // Await.result doesn't handle exceptions in future...
  awaitOnGrindingSuccess("baked beans")

  println("exiting...")
}