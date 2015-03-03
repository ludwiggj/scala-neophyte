package neophyte.typeClasses.take2

import neophyte.typeClasses.take2.Statistics.{NumberLikeInt, NumberLikeDouble, NumberLike}

import scala.language.implicitConversions

object Statistics {

  trait NumberLike[A] {
    def get: A

    def plus(y: NumberLike[A]): NumberLike[A]

    def minus(y: NumberLike[A]): NumberLike[A]

    def divide(y: Int): NumberLike[A]
  }

  case class NumberLikeDouble(x: Double) extends NumberLike[Double] {
    def get: Double = x

    def minus(y: NumberLike[Double]) = NumberLikeDouble(x - y.get)

    def plus(y: NumberLike[Double]) = NumberLikeDouble(x + y.get)

    def divide(y: Int) = NumberLikeDouble(x / y)
  }

  case class NumberLikeInt(x: Int) extends NumberLike[Int] {
    def get: Int = x

    def minus(y: NumberLike[Int]) = NumberLikeInt(x - y.get)

    def plus(y: NumberLike[Int]) = NumberLikeInt(x + y.get)

    def divide(y: Int) = NumberLikeInt(x / y)
  }

  type Quartile[A] = (NumberLike[A], NumberLike[A], NumberLike[A])

  def median[A](xs: Vector[NumberLike[A]]): NumberLike[A] = xs(xs.size / 2)

  def quartiles[A](xs: Vector[NumberLike[A]]): Quartile[A] =
    (xs(xs.size / 4), median(xs), xs(xs.size / 4 * 3))

  def iqr[A](xs: Vector[NumberLike[A]]): NumberLike[A] = quartiles(xs) match {
    case (lowerQuartile, _, upperQuartile) => upperQuartile.minus(lowerQuartile)
  }

  def mean[A](xs: Vector[NumberLike[A]]): NumberLike[A] =
    xs.reduce(_.plus(_)).divide(xs.size)

}

object Main extends App {

  def results[T](v: Vector[NumberLike[T]]) = {
    println(s"Results for $v")
    println(s"Median: ${Statistics.median(v)}")
    println(s"Quartiles: ${Statistics.quartiles(v)}")
    println(s"Iqr: ${Statistics.iqr(v)}")
    println(s"Mean: ${Statistics.mean(v)}")
  }

  implicit def doubleToNumberLikeDouble(d: Double) = NumberLikeDouble(d)
  val v: Vector[NumberLike[Double]] = Vector(1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d)
  results(v)

  implicit def intToNumberLikeInt(i: Int) = NumberLikeInt(i)
  val i: Vector[NumberLike[Int]] = Vector(1, 2, 3, 4, 5, 6, 7, 8)
  results(i)
}