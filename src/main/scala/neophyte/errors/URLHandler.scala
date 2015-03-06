package neophyte.errors

import scala.util.Try
import java.net.URL

object URLHandler extends App {
  def parseURL(url: String): Try[URL] = Try(new URL(url))

  println(parseURL("noGood"))
  println(parseURL("http://www.bbc.co.uk"))

  println(parseURL("ohDear").getOrElse(new URL("http://www.bt.com")))
}