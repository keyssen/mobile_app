package com.nodj.myapplication.ui.theme.product.model

import java.io.Serializable


class Characteristic(val name: String, val value: String)

data class Product(
    val name: String,
    val price: Int,
    val category: String,
    val characteristics: List<Characteristic>,
) : Serializable

fun getProducts(): List<Product> {
    val characteristicList: MutableList<Characteristic> = ArrayList()
    characteristicList.add(Characteristic("characteristic 1", "value 1"))
    characteristicList.add(Characteristic("characteristic 2", "value 2"))
    characteristicList.add(Characteristic("characteristic 3", "value 3"))
    return listOf(
        Product("Product1", 1000, "phone", characteristicList),
        Product("Product2", 10000, "phone", characteristicList),
        Product("Product3", 100000, "phone", characteristicList),
        Product("Product4", 1000, "phone", characteristicList),
        Product("Product5", 10000, "phone", characteristicList),
        Product("Product6", 100000, "phone", characteristicList)
    )
}
