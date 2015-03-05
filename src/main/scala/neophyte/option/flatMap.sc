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
