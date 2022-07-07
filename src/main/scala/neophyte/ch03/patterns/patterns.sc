def gameResults(): Seq[(String, Int)] =
  ("Daniel", 3500) :: ("Melissa", 13000) :: ("John", 7000) :: Nil

def hallOfFame: Seq[String] = for {
  (name, score) <- gameResults()
  if score > 5000
} yield name

hallOfFame

val lists = List(1, 2, 3) :: List.empty :: List(5, 3) :: Nil

for {
  list @ _ :: _ <- lists
} yield list.size

for {
  head :: _ <- lists
} yield head

for {
  _ :: tail <- lists
} yield (1 + tail.size)