package knoldus.service

import knoldus.models.{Inventory, Price, Item}
import scala.concurrent.Future

trait InventoryService {

  def sortByPrice(inventory: Inventory, filterByLowOrHigh: String): Inventory = {

    val sortedItemList: List[Item] = inventory.items.sortBy(_.price.rate)

    if(filterByLowOrHigh.equalsIgnoreCase("low")) {
      Inventory(sortedItemList)
    } else{
      Inventory(sortedItemList.reverse)
    }
  }

  def searchForSomeItem(inventory: Inventory, itemName: String, categoryOfItem: String,
                        filterByPrice: Option[String]): Future[List[Item]] = Future{

    val selectedList: List[Item] = inventory.items.filter(_.category == categoryOfItem).filter(_.name == itemName)

    if(selectedList.isEmpty){
      throw new NoSuchElementException
    }
    else {
      filterByPrice match {
        case Some(filterByPrice) => sortByPrice(Inventory(selectedList), filterByPrice).items
        case None => selectedList
      }
    }
  }

  def priceInformation(inventory: Inventory, itemId: Long): Future[Price] = Future{
    val selectedItem: Option[Item] = inventory.items.find(_.id == itemId)

    selectedItem match {
      case Some(item) => item.price
      case None => throw new NoSuchElementException
    }
  }

  def updateItemCount(inventory: Inventory,itemId: Long, number: Int, f:(Int, Int) => Int): Future[Item] = Future{

    val selectedItem: Option[Item] = inventory.items.find(_.id == itemId)

    selectedItem match {
      case Some(item) => {
        val updatedCount = f(item.count,number)
        val updatedItem = item.copy(count = updatedCount)
        updatedItem
      }
      case None => throw new NoSuchElementException
    }
  }

  def increaseItemCount(value: Int, increaseWith: Int): Int = value + increaseWith

  def decreaseItemCount(value: Int, decreaseWith: Int): Int = value + decreaseWith

  def addItems(inventory: Inventory, name: String, category: String, count: Int, rate: Double, vendor: String): Future[Long] = Future{

    val lastId: Long = inventory.items.map(_.id).max
    val newId: Long = lastId + 1

    val updatedItemList: List[Item] = inventory.items ::: List(Item(newId, name, category, count, Price(newId, rate), vendor))
    val updatedInventory: Inventory = Inventory(updatedItemList)
    newId
  }




}
