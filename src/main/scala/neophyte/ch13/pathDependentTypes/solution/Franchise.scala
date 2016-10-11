package neophyte.ch13.pathDependentTypes.solution

class Franchise(name: String) {

  case class Character(name: String)

  def createFanFictionWith(lovestruck: Character, objectOfDesire: Character): (Character, Character) =
    (lovestruck, objectOfDesire)
}

object Main extends App {
  val starTrek = new Franchise("Star Trek")
  val starWars = new Franchise("Star Wars")

  val quark = starTrek.Character("Quark")
  val jadzia = starTrek.Character("Jadzia Dax")

  val luke = starWars.Character("Luke Skywalker")
  val yoda = starWars.Character("Yoda")

  starTrek.createFanFictionWith(lovestruck = quark, objectOfDesire = jadzia)
  starWars.createFanFictionWith(lovestruck = luke, objectOfDesire = yoda)

  // Following does not compile...
  // Error: type mismatch;
  //   found   : neophyte.pathDependentTypes.solution.Main.starWars.Character
  //   required: neophyte.pathDependentTypes.solution.Main.starTrek.Character
  // starTrek.createFanFictionWith(lovestruck = jadzia, objectOfDesire = luke)

  // Same thing but now with method not defined on Franchise class
  def createFanFiction(f: Franchise)(lovestruck: f.Character, objectOfDesire: f.Character) =
    (lovestruck, objectOfDesire)

  createFanFiction(starTrek)(lovestruck = quark, objectOfDesire = jadzia)

  // Again, disallowed by compiler
  // Error type mismatch;
  // found   : neophyte.pathDependentTypes.solution.Main.starWars.Character
  // required: neophyte.pathDependentTypes.solution.Main.starTrek.Character
  // createFanFiction(starTrek)(lovestruck = quark, objectOfDesire = yoda)
}