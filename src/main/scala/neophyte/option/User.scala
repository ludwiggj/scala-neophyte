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
      s"getUserName: User $id not there"
    }
  }

  println("[getUserName]")
  println(getUserName(1))
  println(getUserName(2))
  println(getUserName(3))

  def getUserFirstNameCareless(id: Int) = {
    val user1 = UserRepository.findById(id)
    try {
      user1.get.firstName
    } catch {
      case x: NoSuchElementException => s"oh dear... ${x.getMessage}"
    }
  }

  println("[getUserFirstNameCareless]")
  println(getUserFirstNameCareless(1))
  println(getUserFirstNameCareless(2))
  println(getUserFirstNameCareless(3))

  def getUserGenderOrDefault(id: Int) = {
    val user1 = UserRepository.findById(id)

    if (user1.isDefined) {
      user1.get.gender.getOrElse({
        "Gender not specified"
      })
    } else {
      s"getUserGenderOrDefault: User $id not there"
    }
  }

  println("[getUserGenderOrDefault]")
  println(getUserGenderOrDefault(1))
  println(getUserGenderOrDefault(2))
  println(getUserGenderOrDefault(3))

  def getUserGenderViaMatch(id: Int) = {
    val user1 = UserRepository.findById(id)

    if (user1.isDefined) {
      user1.get.gender match {
        case Some(gender) => s"Gender $gender"
        case None => "Gender not specified"
      }
    } else {
      s"getUserGenderViaMatch: User $id not there"
    }
  }

  println("[getUserGenderViaMatch]")
  println(getUserGenderViaMatch(1))
  println(getUserGenderViaMatch(2))
  println(getUserGenderViaMatch(3))

  def getUserGenderViaFor(id: Int) = {
    for {
      user <- UserRepository.findById(id)
      gender <- user.gender
    } yield gender
  }

  println("[getUserGenderViaFor]")
  println(getUserGenderViaFor(1))
  println(getUserGenderViaFor(2))
  println(getUserGenderViaFor(3))

  def getUserGenderViaForDesugared(id: Int) = {
    UserRepository.findById(id).flatMap { u =>
      u.gender
    }
  }

  println("[getUserGenderViaForDesugared]")
  println(getUserGenderViaForDesugared(1))
  println(getUserGenderViaForDesugared(2))
  println(getUserGenderViaForDesugared(3))

  def getUserGenderMapped(id: Int) = {
    UserRepository.findById(id).map { u =>
      u.gender
    }
  }

  println("[getUserGenderViaForMapped]")

  println(getUserGenderMapped(1))
  println(getUserGenderMapped(2))
  println(getUserGenderMapped(3))

  def getUserGenders() = {
    for {
      user <- UserRepository.findAll
      gender <- user.gender
    } yield gender
  }

  println("[getUserGenders]")
  println(getUserGenders())

  def getUserGendersDesugared() = {
    UserRepository.findAll.flatMap { u =>
      u.gender
    }
  }

  println("[getUserGendersDesugared]")
  println(getUserGendersDesugared())

  def getUserGendersMapped() = {
    UserRepository.findAll.map { u =>
      u.gender
    }
  }

  println("[getUserGendersMapped]")
  println(getUserGendersMapped())

  def getUserGendersViaMatch() = {
    for {
      User(_, _, _, _, gender) <- UserRepository.findAll
    } yield gender
  }

  println("[getUserGendersViaMatch]")
  println(getUserGendersViaMatch())

  def getUserGendersViaOptionMatch() = {
    for {
      User(_, _, _, _, Some(gender)) <- UserRepository.findAll
    } yield gender
  }

  println("[getUserGendersViaOptionMatch]")
  println(getUserGendersViaOptionMatch())

  def getUserGendersViaOptionMatchDesugared() = {
    UserRepository.findAll.flatMap { case User(_, _, _, _, gender) =>
      gender
    }
  }

  println("[getUserGendersViaOptionMatchDesugared]")
  println(getUserGendersViaOptionMatchDesugared())

  def printUserName(id: Int) = {
    UserRepository.findById(id).foreach(user => println(user.firstName))
  }

  println("[printUserName]")
  printUserName(1)
  printUserName(2)
  printUserName(3)

  def getUserAge(id: Int) = {
    UserRepository.findById(id).map(_.age)
  }

  println("[getUserAge]")
  println(getUserAge(1))
  println(getUserAge(2))
  println(getUserAge(3))

  def getUserGenderViaMap(id: Int) = {
    UserRepository.findById(id).map(_.gender)
  }

  println("[getUserGenderViaMap]")
  println(getUserGenderViaMap(1))
  println(getUserGenderViaMap(2))
  println(getUserGenderViaMap(3))

  def getUserGenderViaFlatMap(id: Int) = {
    UserRepository.findById(id).flatMap(_.gender)
  }

  println("[getUserGenderViaFlatMap]")
  println(getUserGenderViaFlatMap(1))
  println(getUserGenderViaFlatMap(2))
  println(getUserGenderViaFlatMap(3))

  def userOlderThat30(id: Int) = {
    UserRepository.findById(id).filter(_.age > 30)
  }

  println("[userOlderThat30]")
  println(userOlderThat30(1))
  println(userOlderThat30(2))
  println(userOlderThat30(3))


  case class Resource(content: String)

  val resourceFromConfigDir: Option[Resource] = None
  val resourceFromClasspath: Option[Resource] = Some(Resource("I was found on the classpath"))

  val resource = resourceFromConfigDir orElse resourceFromClasspath

  println(s"Resource: $resource")
}