package com.gildedrose

class GildedRose(val items: Array[Item]) {
  def updateQuality() {
    for (i <- items.indices) {
      items(i) = myMap(items(i).name)(items(i))
    }
  }

  type ItemUpdater = Item => Item
  private val defaultLogic: ItemUpdater = defaultBehaviour(decreaseQuality(_))
  private val agedBrieLogic: ItemUpdater = defaultBehaviour(increaseQuality(_))
  private val passesLogic: ItemUpdater = qualityInBounds(sellInDecrease(handleBackstagePasses))
  private val ragnarosLogic: ItemUpdater = (item: Item) => item

  private def defaultBehaviour(itemUpdater: ItemUpdater): ItemUpdater =
    conjuredDecreaseTwiceAsFast(
      qualityInBounds(
        sellInDecrease(
          qualityTwiceAsFastAfterSellIn(itemUpdater))))

  val myMap: Map[String, ItemUpdater] = Map(
    "Aged Brie" -> agedBrieLogic,
    "Backstage passes to a TAFKAL80ETC concert" -> passesLogic,
    "Sulfuras, Hand of Ragnaros" -> ragnarosLogic
  )
    .withDefaultValue(defaultLogic)

  private def qualityInBounds(itemUpdater: ItemUpdater): ItemUpdater = item => {
    val newItem = itemUpdater(item)
    Item(newItem.name, newItem.sellIn, newItem.quality min 50 max 0)
  }

  private def sellInOK(item: Item): Boolean = item.sellIn > 0
  private def qualityTwiceAsFastAfterSellIn(itemUpdater: ItemUpdater): ItemUpdater = item => {
    val newItem = itemUpdater(item)
    if (sellInOK(item)) {
      newItem
    } else {
      val qualityDifference = item.quality - newItem.quality
      decreaseQuality(newItem, qualityDifference)
    }
  }

  private def isConjured(item: Item): Boolean = item.name.toLowerCase.contains("conjured")
  private def conjuredDecreaseTwiceAsFast(itemUpdater: ItemUpdater): ItemUpdater = item => {
    val newItem = itemUpdater(item)
    if (!isConjured(item)) {
      newItem
    } else {
      val qualityDifference = item.quality - newItem.quality
      decreaseQuality(newItem, qualityDifference)
    }
  }

  private def sellInDecrease(itemUpdater: ItemUpdater): ItemUpdater = item => {
    val newItem = itemUpdater(item)
    Item(newItem.name, newItem.sellIn - 1, newItem.quality)
  }

  private def handleBackstagePasses(item: Item): Item = {
    item.sellIn match {
      case sellIn@_ if sellIn <= 0 => new Item(item.name, sellIn, 0)
      case sellIn@_ if sellIn <= 5 => increaseQuality(item,3)
      case sellIn@_ if sellIn <= 10 => increaseQuality(item,2)
      case _ => increaseQuality(item)
    }
  }

  private def decreaseQuality(item: Item, n: Int = 1): Item = {
    Item(item.name, item.sellIn, item.quality - n)
  }

  private def increaseQuality(item: Item,n: Int = 1): Item = {
    Item(item.name, item.sellIn, item.quality + n)
  }
}