package neophyte.ch13.pathDependentTypes.solutionClunky

object Franchise {
  case class Character(name: String, franchise: Franchise)
}

class Franchise(name: String) {

  import Franchise.Character

  def createFanFiction(lovestruck: Character, objectOfDesire: Character): (Character, Character) = {
    require(lovestruck.franchise == objectOfDesire.franchise)
    (lovestruck, objectOfDesire)
  }
}

object Main2 extends App {
  val starTrek = new Franchise("Star Trek")
  val starWars = new Franchise("Star Wars")

  val quark = Franchise.Character("Quark", starTrek)
  val jadzia = Franchise.Character("Jadzia Dax", starTrek)

  val luke = Franchise.Character("Luke Skywalker", starWars)
  val yoda = Franchise.Character("Yoda", starWars)

  starTrek.createFanFiction(lovestruck = yoda, objectOfDesire = luke)

  // This is banned, fails compilation
  starTrek.createFanFiction(lovestruck = jadzia, objectOfDesire = luke)
}