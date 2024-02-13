package com.achsanit.gxsales.data.local.entity

data class ShopItemEntity(
    val id: Int,
    val title: String,
    val price: String,
    val stock: Int,
    val type: String,
    val taxAmount: String,
    val taxPercentage: Int,
    val image: Int
)
