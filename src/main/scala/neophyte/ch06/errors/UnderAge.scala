package neophyte.ch06.errors

case class Customer(age:Int)
class Cigarettes
case class UnderAgeException(message:String) extends Exception(message)

object Smokes extends App {

  def buyCigarettes(customer: Customer): Cigarettes =
    if (customer.age < 16) throw UnderAgeException(s"Customer must be older than 16 but was ${customer.age}")
    else new Cigarettes

  def buyCigarettesWithCatch(customer: Customer) =
  try {
    buyCigarettes(customer)
    "Yo, happy smoking"
  } catch {
    case UnderAgeException(msg) => msg
  }

  println("[buyCigarettesWithCatch]")
  println(buyCigarettesWithCatch(Customer(21)))
  println(buyCigarettesWithCatch(Customer(14)))

  println("[buyCigarettes]")
  println(buyCigarettes(Customer(21)))
  println(buyCigarettes(Customer(14)))
}