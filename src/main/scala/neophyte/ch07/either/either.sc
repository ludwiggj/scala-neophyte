import java.net.URL

type Citizen = String

case class BlackListedResource(url: URL, visitors: Set[Citizen])

val blacklist = List(
  BlackListedResource(
    new URL("https://google.com"), Set("John Doe", "Johanna Doe")),
  BlackListedResource(
    new URL("http://yahoo.com"), Set.empty),
  BlackListedResource(
    new URL("https://maps.google.com"), Set("John Doe")),
  BlackListedResource(
    new URL("http://plus.google.com"), Set.empty)
)

val checkedBlacklist: List[Either[URL, Set[Citizen]]] =
  blacklist.map(resource =>
    if (resource.visitors.isEmpty) Left(resource.url)
    else Right(resource.visitors))

checkedBlacklist.map(x => x.left.toOption)

val suspiciousResources: List[URL] = checkedBlacklist.flatMap(x => x.left.toOption)

val problemCitizens = checkedBlacklist.flatMap(_.right.toOption).flatten.toSet