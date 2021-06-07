import org.specs2.mutable.Specification

//ordered tests

class OrderedTests extends Specification {
  sequential

  "List" should {
    var list = List[Int]()
    "increase it's length" in {
      list = 1 :: list
      list must haveSize(1)
      list = 2 :: list
      list must haveSize(2)
    }
  }

  "Map" should {
    var map = Map[String, String]()
    "satisfy get set contract" in {
      map = map.+("k" -> "v")
      map.get("k") must beSome("v")
    }

    "delete value" in {
      map = map.-("k")
      map.get("k") must beNone
    }
  }
}
