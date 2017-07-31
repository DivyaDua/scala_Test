package knoldus.models

import knoldus.models.Price

case class Item(id: Long, name: String, category: String, count: Int, price: Price, vendor: String)

