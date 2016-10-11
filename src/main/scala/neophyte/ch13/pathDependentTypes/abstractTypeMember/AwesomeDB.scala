package neophyte.ch13.pathDependentTypes.abstractTypeMember

object AwesomeDB {

  abstract class Key(name: String) {
    type Value
  }

}

import AwesomeDB.Key

class AwesomeDB {

  import collection.mutable.Map

  val data = Map.empty[Key, Any]

  def get(key: Key): Option[key.Value] =
    data.get(key).asInstanceOf[Option[key.Value]]

  def set(key: Key)(value: key.Value): Unit =
    data.update(key, value)

  override
  def toString() = {
    data.mkString(" | ")
  }
}

trait IntValued extends Key {
  type Value = Int
}

trait StringValued extends Key {
  type Value = String
}

object Keys {
  val foo = new Key("foo") with IntValued
  val bar = new Key("bar") with StringValued
}

object Main extends App {
  val dataStore = new AwesomeDB
  dataStore.set(Keys.foo)(23)
  val i: Option[Int] = dataStore.get(Keys.foo)

  // does not compile
  // Error:type mismatch;
  // found   : String("23")
  // required: neophyte.pathDependentTypes.abstractTypeMember.Keys.foo.Value
  //    (which expands to)  Int
  // dataStore.set(Keys.foo)("23")

  dataStore.set(Keys.bar)("2345")

  System.out.println(dataStore)
}