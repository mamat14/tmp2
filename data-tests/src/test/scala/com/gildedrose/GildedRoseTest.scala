package com.gildedrose

import org.scalatest._
import org.scalatest.prop.PropertyChecks
import org.scalacheck.{Gen, Shrink}

class GildedRoseTest extends FlatSpec with Matchers with PropertyChecks {
  implicit val disableShrink: Shrink[String] = Shrink(_ => Stream.empty)
  implicit val disableShrink2: Shrink[Int] = Shrink(_ => Stream.empty)
  implicit override val generatorDrivenConfig: PropertyCheckConfiguration =
    PropertyCheckConfiguration(minSize = 100, sizeRange = 100)

  val qualityGenerator: Gen[Int] = Gen.choose(0, 50)
  val sellInGenerator: Gen[Int] = Gen.choose(-10, 10)
  val nameGenerator: Gen[String] = Gen.oneOf(
    "foo",
    "Backstage passes to a TAFKAL80ETC concert",
    "Sulfuras, Hand of Ragnaros",
    "Aged Brie"
  )

  val itemGenerator: Gen[Item] = for {
    name <- nameGenerator
    quality <- qualityGenerator
    sellIn <- sellInGenerator
  } yield Item(name, sellIn, quality)

  it should "degrade quality" in {
    forAll((Gen.const("foo"), "name"), (sellInGenerator, "sellIn"), (qualityGenerator, "quality")) { (name, sellIn, quality) =>
      whenever(quality > 0) {
        val items = Array[Item](Item(name, sellIn, quality))
        val app = new GildedRose(items)
        app.updateQuality()
        app.items(0).quality should be < quality
      }
    }
  }

  it should "degradeQuality twice as fast after sellIn date is passed" in {
    forAll((Gen.const("foo"), "name"), (Gen.const(1), "sellIn"), (qualityGenerator, "quality")) { (name, sellIn, quality) =>
      val items = Array[Item](Item(name, sellIn, quality))
      val app = new GildedRose(items)

      app.updateQuality()

      val qualityAfterFirstDate = app.items(0).quality
      val deltaBeforeSellIn = quality - qualityAfterFirstDate

      app.updateQuality()

      val qualityAfterSecondDate = app.items(0).quality
      val deltaAfterSellIn = qualityAfterFirstDate - qualityAfterSecondDate

      if (qualityAfterSecondDate > 0) deltaAfterSellIn should equal(deltaBeforeSellIn * 2)
      else qualityAfterSecondDate should equal(0)
    }
  }

  it should "never be negative" in {
    forAll((itemGenerator, "item")) { item =>
      val items = Array[Item](item)
      val app = new GildedRose(items)
      app.updateQuality()
      app.items(0).quality should be >= 0
    }
  }

  it should "increase Aged Brie quality" in {
    forAll((Gen.const("Aged Brie"), "name"), (sellInGenerator, "sellIn"), (qualityGenerator, "quality")) { (name, sellIn, quality) =>
      whenever(quality < 50) {
        val items = Array[Item](Item(name, sellIn, quality))
        val app = new GildedRose(items)
        app.updateQuality()
        app.items(0).quality should be > quality
      }
    }
  }

  it should "never set quality of an item to more than 50" in {
    forAll((itemGenerator, "item")) { item =>
      whenever(item.name != "Sulfuras, Hand of Ragnaros") {
        val items = Array[Item](item)
        val app = new GildedRose(items)
        app.items(0).quality should be <= 50
      }
    }
  }

  it should "never change \"Sulfuras\" quality value" in {
    forAll(
      (Gen.const("Sulfuras, Hand of Ragnaros"), "name"),
      (sellInGenerator, "sellIn"),
      (Gen.const(80), "quality")) { (name, sellIn, quality) =>
      val items = Array[Item](Item(name, sellIn, quality))
      val app = new GildedRose(items)
      app.updateQuality()

      app.items(0).quality should equal(quality)
    }
  }

  it should "... \"Sulfuras\" quality always 80" in {
    forAll(
      (Gen.const("Sulfuras, Hand of Ragnaros"), "name"),
      (sellInGenerator, "sellIn"),
      (Gen.const(80), "quality")) { (name, sellIn, quality) =>
      val items = Array[Item](Item(name, sellIn, quality))
      val app = new GildedRose(items)

      app.updateQuality()

      app.items(0).quality should equal(quality)
    }
  }

  it should "increase \"Backstage passes\" quality by 2 if sellIn is 6" in {
    forAll(
      (Gen.const("Backstage passes to a TAFKAL80ETC concert"), "name"),
      (Gen.choose(6, 10), "sellIn"),
      (qualityGenerator, "quality")) { (name: String, sellIn: Int, quality: Int) =>
      val items = Array[Item](Item(name, sellIn, quality))
      val app = new GildedRose(items)
      app.updateQuality()

      app.items(0).quality should equal(quality + 2 min 50)
    }
  }


  it should "increase \"Backstage passes\" quality by 3 if sellIn from 1 to 5" in {
    forAll(
      (Gen.const("Backstage passes to a TAFKAL80ETC concert"), "name"),
      (Gen.chooseNum(1, 5), "sellIn"),
      (qualityGenerator, "quality")) { (name, sellIn, quality) =>
      val items = Array[Item](Item(name, sellIn, quality))
      val app = new GildedRose(items)
      app.updateQuality()

      app.items(0).quality should equal(quality + 3 min 50)
    }
  }

  it should "set \"Backstage passes\" quality to 0 " in {
    forAll(
      (Gen.const("Backstage passes to a TAFKAL80ETC concert"), "name"),
      (Gen.const(0), "sellIn"),
      (qualityGenerator, "quality")) { (name, sellIn, quality) =>
      val items = Array[Item](Item(name, sellIn, quality))
      val app = new GildedRose(items)

      app.updateQuality()

      app.items(0).quality should equal(0)
    }
  }

  it should "degrade conjured items twice as fast as normal items" in {
    forAll((itemGenerator, "item")) { item =>
      whenever(item.name == "foo") {
        val quality = item.quality
        val items = Array[Item](
          item,
          makeConjured(item)
        )

        val app = new GildedRose(items)
        app.updateQuality()

        val differenceInQualityForNotConjured = quality - app.items(0).quality
        val differenceInQualityForConjured = quality - app.items(1).quality

        differenceInQualityForConjured should equal(differenceInQualityForNotConjured * 2)
      }
    }
  }

  private def makeConjured(item: Item): Item = Item(s"conjured ${item.name}", item.sellIn, item.quality)
}