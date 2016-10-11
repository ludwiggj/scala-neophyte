package neophyte.ch13.pathDependentTypes.problem

object Franchise {
  case class Character(name: String)
}

class Franchise(name: String) {

  import Franchise.Character

  def createFanFiction(lovestruck: Character, objectOfDesire: Character): (Character, Character) =
    (lovestruck, objectOfDesire)
}

object Main extends App {
  val starTrek = new Franchise("Star Trek")
  val starWars = new Franchise("Star Wars")

  val quark = Franchise.Character("Quark")
  val jadzia = Franchise.Character("Jadzia Dax")

  val luke = Franchise.Character("Luke Skywalker")
  val yoda = Franchise.Character("Yoda")

  starTrek.createFanFiction(lovestruck = jadzia, objectOfDesire = luke)
}