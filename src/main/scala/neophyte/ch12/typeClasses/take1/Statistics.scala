package neophyte.ch12.typeClasses.take1

object Statistics {
  def median(xs: Vector[Double]): Double = xs(xs.size / 2)

  def quartiles(xs: Vector[Double]): (Double, Double, Double) =
    (xs(xs.size / 4), median(xs), xs(xs.size / 4 * 3))

  def iqr(xs: Vector[Double]): Double = quartiles(xs) match {
    case (lowerQuartile, _, upperQuartile) => upperQuartile - lowerQuartile
  }

  def mean(xs: Vector[Double]): Double = {
    xs.reduce(_ + _) / xs.size
  }
}

object Main extends App {
  val v = Vector(1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d)

  println(s"Results for $v")
  println(s"Median: ${Statistics.median(v)}")
  println(s"Quartiles: ${Statistics.quartiles(v)}")
  println(s"Iqr: ${Statistics.iqr(v)}")
  println(s"Mean: ${Statistics.mean(v)}")
}