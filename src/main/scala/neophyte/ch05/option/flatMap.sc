val names: List[List[String]] =
  List(
    List("John", "Johanna", "Daniel"),
    List(),
    List("Doe", "Westheide")
  )

names.map(_.map(_.toUpperCase))
names.flatMap(_.map(_.toUpperCase))

val names2:List[Option[String]]=
  List(
    Some("Johanna"), None, Some("Daniel")
  )

names2.map(_.map(_.toUpperCase))
names2.flatMap(_.map(_.toUpperCase))

case class User(
 id: Int,
 firstName: String,
 lastName: String,
 age: Int,
 gender: Option[String])

object UserRepository {
 private val users = Map(
 1 -> User(1, "John", "Doe", 32, Some("male")),
 2 -> User(2, "Johanna", "Doe", 30, None))

 def findById(id: Int): Option[User] = users.get(id)
 def findAll: List[User] = users.values.toList
}

for {
  user <- UserRepository.findById(1)
  gender <- user.gender
} yield gender

for {
  user <- UserRepository.findAll
  gender <- user.gender
} yield gender
