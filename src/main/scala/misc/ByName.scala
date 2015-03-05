package misc

object ByName extends App {

  def nano() = {
    println("Getting nano")
    System.nanoTime
  }

  def delayed(t: => Long) = {
    // => indicates a by-name parameter

    // ... argument is not evaluated at the point of function application,
    // but instead is evaluated at each use within the function.
    println("In delayed method")
    println("Param: " + t) // t or nano() used, so "Getting nano" printed,
    // then print "Param: " and the return of nano(), e.g. System.nanoTime
    t // t or nano() used again, thus print "Getting nano" and the System.nanoTime
    // would have now elapsed
  }

  println(delayed(nano()))


  // Example output...

  //In delayed method
  //Getting nano
  //Param: 1425500359310780000
  //Getting nano
  //1425500359310938000

  def something() = {
    println("calling something")
    1 // return value
  }

  def callByValue(x: Int) = {
    println("x1=" + x)
    println("x2=" + x)
  }

  def callByName(x: => Int) = {
    println("x1=" + x)
    println("x2=" + x)
  }

  println("Call by name")
  callByName(something())
  println("Call by value")
  callByValue(something())
}