package neophyte.ch13.pathDependentTypes.solution

class A {
  class B
  var b: Option[B] = None
}

object Main3 extends App {
  val a1 = new A
  val a2 = new A
  val b1 = new a1.B
  val b2 = new a2.B
  a1.b = Some(b1)

  // does not compile
  // Error: type mismatch;
  // found   : neophyte.pathDependentTypes.Main3.a1.B
  // required: neophyte.pathDependentTypes.Main3.a2.B
  //  a2.b = Some(b1)

  // a2.b = Some(b1)
}