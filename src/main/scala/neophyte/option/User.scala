package neophyte.option

import java.util.NoSuchElementException

case class User(id: Int,
                firstName: String,
                lastName: String,
                age: Int,
                gender: Option[String])

object UserRepository {
  private val users = Map(
    1 -> User(1, "John", "Doe", 32, Some("male")),
    2 -> User(2, "Johanna", "Doe", 30, None)
  )

  def findById(id: Int): Option[User] = users.get(id)

  def findAll = users.values
}

object TestUserRepo extends App {
  def getUserName(id: Int) = {
    val user1 = UserRepository.findById(id)

    if (user1.isDefined) {
      user1.get.firstName
    } else {
      "User not there"
    }
  }

  def getUserFirstNameCareless(id: Int) = {
    val user1 = UserRepository.findById(id)
    try {
      user1.get.firstName
    } catch {
      case x: NoSuchElementException => s"oh dear... ${x.getMessage}"
    }
  }

  def getUserGenderOrDefault(id: Int) = {
    val user1 = UserRepository.findById(id)

    if (user1.isDefined) {
      user1.get.gender.getOrElse({
        println("Evaluating default")
        "Gender not specified"
      })
    } else {
      "User not there"
    }
  }

  def getUserGenderViaMatch(id: Int) = {
    val user1 = UserRepository.findById(id)

    if (user1.isDefined) {
      user1.get.gender match {
        case Some(gender) => s"Gender $gender"
        case None => "Gender not specified"
      }
    } else {
      "User not there"
    }
  }

  def getUserGenderViaFor(id: Int) = {
    for {
      user <- UserRepository.findById(id)
      gender <- user.gender
    } yield gender
  }

  def getUserGenderViaForDesugared(id: Int) = {
    UserRepository.findById(id).flatMap { u =>
      u.gender
    }
  }

  def getUserGenders() = {
    for {
      user <- UserRepository.findAll
      gender <- user.gender
    } yield gender
  }

  def getUserGendersDesugared() = {
    UserRepository.findAll.flatMap { u =>
      u.gender
    }
  }

  def getUserGendersViaMatch1() = {
    for {
      User(_, _, _, _, gender) <- UserRepository.findAll
    } yield gender
  }

  def getUserGendersViaMatch2() = {
    for {
      User(_, _, _, _, Some(gender)) <- UserRepository.findAll
    } yield gender
  }

  def getUserGendersViaMatch2Desugared() = {
    UserRepository.findAll.flatMap { case User(_, _, _, _, gender) =>
      gender
    }
  }

  def printUserName(id: Int): Unit = {
    UserRepository.findById(id).foreach(user => println(user.firstName))
  }

  def getUserAge(id: Int) = {
    UserRepository.findById(id).map(_.age)
  }

  def getUserGenderViaMap(id: Int) = {
    UserRepository.findById(id).map(_.gender)
  }

  def getUserGenderViaFlatMap(id: Int) = {
    UserRepository.findById(id).flatMap(_.gender)
  }

  def userOlderThat30(id: Int) = {
    UserRepository.findById(id).filter(_.age > 30)
  }

  println(getUserName(1))
  println(getUserName(2))
  println(getUserName(3))
  println(getUserFirstNameCareless(1))
  println(getUserFirstNameCareless(2))
  println(getUserFirstNameCareless(3))
  println(getUserGenderOrDefault(1))
  println(getUserGenderOrDefault(2))
  println(getUserGenderOrDefault(3))
  println(getUserGenderViaMatch(1))
  println(getUserGenderViaMatch(2))
  println(getUserGenderViaMatch(3))
  printUserName(1)
  printUserName(2)
  printUserName(3)
  println(getUserAge(1))
  println(getUserAge(2))
  println(getUserAge(3))
  println(getUserGenderViaMap(1))
  println(getUserGenderViaMap(2))
  println(getUserGenderViaMap(3))
  println(getUserGenderViaFlatMap(1))
  println(getUserGenderViaFlatMap(2))
  println(getUserGenderViaFlatMap(3))
  println(userOlderThat30(1))
  println(userOlderThat30(2))
  println(userOlderThat30(3))
  println(getUserGenderViaFor(1))
  println(getUserGenderViaFor(2))
  println(getUserGenderViaFor(3))
  println(getUserGenderViaForDesugared(1))
  println(getUserGenderViaForDesugared(2))
  println(getUserGenderViaForDesugared(3))
  println(getUserGenders())
  println(getUserGendersDesugared())
  println(getUserGendersViaMatch1())
  println(getUserGendersViaMatch2())
  println(getUserGendersViaMatch2Desugared())

  case class Resource(content:String)
  val resourceFromConfigDir:Option[Resource]= None
  val resourceFromClasspath:Option[Resource]= Some(Resource("I was found on the classpath"))

  val resource = resourceFromConfigDir orElse resourceFromClasspath
  
  println(resource)
}