case class Email(subject: String,
                 text: String,
                 sender: String,
                 recipient: String)

type EmailFilter = Email => Boolean

def newMailsForUser(mails: Seq[Email], f: EmailFilter): Seq[Email] = mails.filter(f)

val sentByOneOf: Set[String] => EmailFilter =
  senders => email => senders.contains(email.sender)
val notSentByAnyOf: Set[String] => EmailFilter =
  senders => email => !senders.contains(email.sender)
val minimumSize: Int => EmailFilter = n => email => email.text.size >= n
val maximumSize: Int => EmailFilter = n => email => email.text.size <= n

val mails = Email(
  subject = "It's me again, your stalker friend!",
  text = "Hello my friend! How are you?",
  sender = "johndoe@example.com",
  recipient = "me@example.com") :: Nil

val emailFilter1: EmailFilter = notSentByAnyOf(Set("johndoe@example.com"))

newMailsForUser(mails, emailFilter1)

type SizeChecker = Int => Boolean

val sizeConstraint: SizeChecker => EmailFilter =
  f => email => f(email.text.size)

val minimumSize1: Int => EmailFilter = n => sizeConstraint(_ >= n)
val maximumSize2: Int => EmailFilter = n => sizeConstraint(_ <= n)

val emailFilter2: EmailFilter = minimumSize1(10)

newMailsForUser(mails, emailFilter2)

def complement[A](predicate: A => Boolean) = (a: A) => !predicate(a)

val notSentByAnyOf2 = sentByOneOf andThen (g => complement(g))

val emailFilter3: EmailFilter = sentByOneOf(Set("johndoe@example.com"))

val emailFilter4: EmailFilter = notSentByAnyOf2(Set("johndoe@example.com"))

newMailsForUser(mails, emailFilter3)

newMailsForUser(mails, emailFilter4)

def any[A](predicates: (A => Boolean)*): A => Boolean =
  a => predicates.exists(pred => pred(a))
def none[A](predicates: (A => Boolean)*) =
  complement(any(predicates: _*))
def every[A](predicates: (A => Boolean)*) =
  none(predicates.view.map(x => complement(x)): _*)

val filter: EmailFilter = every(
  notSentByAnyOf(Set("johndoe@example.com")),
  minimumSize(100),
  maximumSize(10000)
)

val addMissingSubject = (email: Email) =>
  if (email.subject.isEmpty) email.copy(subject = "No subject")
  else email
val checkSpelling = (email: Email) =>
  email.copy(text = email.text.replaceAll("your", "you're"))
val removeInappropriateLanguage = (email: Email) =>
  email.copy(
    text = email.text.replaceAll("dynamic typing", "**CENSORED**"))
val addAdvertismentToFooter = (email: Email) =>
  email.copy(
    text = email.text + "\nThis mail sent via Super Awesome Free Mail")

val pipeline: Email => Email = Function.chain(Seq(
  addMissingSubject,
  checkSpelling,
  removeInappropriateLanguage,
  addAdvertismentToFooter))