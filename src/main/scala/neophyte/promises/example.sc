import neophyte.promises.TaxCut
import concurrent.Future
import concurrent.ExecutionContext.Implicits.global
val f: Future[String] = Future { "Hello world"}
import concurrent.Promise
val taxcut = Promise[TaxCut]()

val taxcut2: Promise[TaxCut] = Promise()

val taxcutF: Future[TaxCut] = taxcut.future

taxcut.success(TaxCut(12))

//taxcut.success(TaxCut(5))