package neophyte.ch12.typeClasses.solution

object Math {

  import scala.annotation.implicitNotFound

  @implicitNotFound("No member of type class NumberLike in scope for ${T}")
  trait NumberLike[T] {
    def plus(x: T, y: T): T

    def divide(x: T, y: Int): T

    def minus(x: T, y: T): T
  }

}

import Math._

object NumberLike {

  implicit object NumberLikeDouble extends NumberLike[Double] {
    override def plus(x: Double, y: Double): Double = x + y

    override def divide(x: Double, y: Int): Double = x / y

    override def minus(x: Double, y: Double): Double = x - y
  }

  implicit object NumberLikeInt extends NumberLike[Int] {
    override def plus(x: Int, y: Int): Int = x + y

    override def divide(x: Int, y: Int): Int = x / y

    override def minus(x: Int, y: Int): Int = x - y
  }

}

import NumberLike._

object Statistics {
  def mean[T](xs: Vector[T])(implicit ev: NumberLike[T]): T =
    ev.divide(xs.reduce(ev.plus(_, _)), xs.size)

  // [T: NumberLike] is a context bound
  // This is a shortcut for implicit parameters with only one type parameter
  def median[T: NumberLike](xs: Vector[T]): T = xs(xs.size / 2)

  def quartiles[T: NumberLike](xs: Vector[T]): (T, T, T) =
    (xs(xs.size / 4), median(xs), xs(xs.size / 4 * 3))

  def iqr[T: NumberLike](xs: Vector[T]): T = quartiles(xs) match {
    case (lowerQuartile, _, upperQuartile) =>
      implicitly[NumberLike[T]].minus(upperQuartile, lowerQuartile)
  }
}

object JodaImplicits {

  import org.joda.time.Duration

  implicit object NumberLikeDuration extends NumberLike[Duration] {
    def plus(x: Duration, y: Duration): Duration = x.plus(y)

    def divide(x: Duration, y: Int): Duration = Duration.millis(x.getMillis / y)

    def minus(x: Duration, y: Duration): Duration = x.minus(y)
  }

}

object Main extends App {
  def results[T](v: Vector[T])(implicit ev: NumberLike[T]) = {
    println(s"Results for $v")
    println(s"Median: ${Statistics.median(v)}")
    println(s"Quartiles: ${Statistics.quartiles(v)}")
    println(s"Iqr: ${Statistics.iqr(v)}")
    println(s"Mean: ${Statistics.mean(v)}")
  }


  val doubles = Vector[Double](13, 23.0, 42, 45, 61, 73, 96, 100, 199, 420, 900, 3839)
  results(doubles)

  val ints = Vector[Int](13, 23, 42, 45, 61, 73, 96, 100, 199, 420, 900, 3839)
  results(ints)

  val strings = Vector[String]("The", "cat", "in", "the", "hat")

  // Following invocation results in compilation error:
  // Error:(88, 12) No member of type class NumberLike in scope for String

  // results(strings)

  import JodaImplicits._
  import org.joda.time.Duration._

  val durations = Vector(standardSeconds(20), standardSeconds(57), standardMinutes(2),
    standardMinutes(17), standardMinutes(30), standardMinutes(58), standardHours(2),
    standardHours(5), standardHours(8), standardHours(17), standardDays(1),
    standardDays(4))
  println(Statistics.mean(durations).getStandardHours)
}