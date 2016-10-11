package neophyte.ch06.errors

import java.io.{FileNotFoundException, InputStream}
import java.net.{MalformedURLException, URL}

import scala.io.Source
import scala.util.{Failure, Success, Try}

object URLHandler extends App {
  def parseURL(url: String): Try[URL] = Try(new URL(url))

  println("[parseURL]")
  println(parseURL("noGood"))
  println(parseURL("http://www.bbc.co.uk"))

  println("[parseURL.getOrElse]")
  println(parseURL("ohDear").getOrElse(new URL("http://www.bt.com")))
  println(parseURL("http://www.bbc.co.uk").getOrElse(new URL("http://www.bt.com")))

  println("[parseURL.map]")
  println(parseURL("http://www.bbc.co.uk").map(_.getProtocol))
  println(parseURL("oops").map(_.getProtocol))

  def inputStreamForURL(url: String): Try[Try[Try[InputStream]]] = {
    parseURL(url).map { u =>
      Try(u.openConnection()).map(conn => Try(conn.getInputStream()))
    }
  }

  println("[inputStreamForURL]")
  println(inputStreamForURL("noDice"))
  println(inputStreamForURL("http://www.bbc.co.uk"))

  def inputStreamForURLFlatMapped(url: String): Try[InputStream] = {
    parseURL(url).flatMap { u =>
      Try(u.openConnection()).flatMap(conn => Try(conn.getInputStream()))
    }
  }

  println("[inputStreamForURLFlatMapped]")
  println(inputStreamForURLFlatMapped("noDice"))
  println(inputStreamForURLFlatMapped("http://www.bbc.co.uk"))

  def inputStreamForURLMapFlatMap(url: String): Try[Try[InputStream]] = {
    parseURL(url).map { u =>
      Try(u.openConnection()).flatMap(conn => Try(conn.getInputStream()))
    }
  }

  println("[inputStreamForURLMapFlatMap]")
  println(inputStreamForURLMapFlatMap("noDice"))
  println(inputStreamForURLMapFlatMap("http://www.bbc.co.uk"))

  def inputStreamForURLFlatMapMap(url: String): Try[Try[InputStream]] = {
    parseURL(url).flatMap { u =>
      Try(u.openConnection()).map(conn => Try(conn.getInputStream()))
    }
  }

  println("[inputStreamForURLFlatMapMap]")
  println(inputStreamForURLFlatMapMap("noDice"))
  println(inputStreamForURLFlatMapMap("http://www.bbc.co.uk"))

  def parseHttpURL(url: String): Try[URL] = parseURL(url).filter(_.getProtocol == "http")

  println("[parseHttpURL]")
  println(parseHttpURL("noGood"))
  println(parseHttpURL("http://www.bbc.co.uk"))
  println(parseHttpURL("ftp://mirror.ntecologne.de/apache.org"))

  def parseHttpURLForEach(url: String) = {
    parseHttpURL(url).foreach(println)
    parseHttpURL(url).foreach(println(_))
    parseHttpURL(url).foreach(x => println(s"Yay, $url has http protocol"))
    parseHttpURL(url).foreach(x => println(s"Yay, $x has http protocol"))
  }

  println("[parseHttpURLForEach]")
  parseHttpURLForEach("http://www.bbc.co.uk")
  parseHttpURLForEach("https://www.bbc.co.uk")

  def getURLContent(url: String): Try[Iterator[String]] = {
    for {
      url <- parseURL(url)
      connection <- Try(url.openConnection())
      is <- Try(connection.getInputStream)
      source = Source.fromInputStream(is)
    } yield source.getLines()
  }

  println("[getURLContent]")
  getURLContent("http://www.bbc.co.uk").map(_.next()).foreach(println)
  getURLContent("none").map(_.next()).foreach(println)

  def getURLContentDesugared(url: String): Try[Iterator[String]] = {
    parseURL(url).flatMap(url =>
      Try(url.openConnection()).flatMap(connection =>
        Try(connection.getInputStream).map(is =>
          (is, Source.fromInputStream(is))).map(source =>
          source match {
            case (_, source) => source.getLines()
          }
          )
      )
    )
  }

  println("[getURLContentDesugared]")
  getURLContentDesugared("http://www.bbc.co.uk").map(_.next()).foreach(println)
  getURLContentDesugared("none").map(_.next()).foreach(println)

  def displayPage(url: String) = {
    getURLContent(url) match {
      case Success(lines) => lines.foreach(println)
      case Failure(ex) => println(s"Problem rendering URL content ${ex.getMessage}")
    }
  }

  println("[displayPage]")
  displayPage("http://www.bbc.co.uk")
  displayPage("none")
  displayPage("http://www.bbc.co.uk/notThere")

  def displayPageWithRecover(url: String) = {
    getURLContent(url) recover {
        case e: FileNotFoundException => Iterator("Requested page does not exist")
        case e: MalformedURLException => Iterator("Please enter a valid URL")
        case _ => Iterator("An unexpected problem has occurred")
    }
  }

  println("[displayPageWithRecover]")
  println(displayPageWithRecover("http://www.bbc.co.uk").get.size)
  displayPageWithRecover("none").get.foreach(println)
  displayPageWithRecover("http://www.bbc.co.uk/notThere").get.foreach(println)
}