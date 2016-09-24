import org.scalatest._

class cotaSpec extends FlatSpec with Matchers {

  "hello world" should "return hiya" in {
    val result = Cota.helloWorld()
    result should equal "hiya"
  }

}
