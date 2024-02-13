package com.achsanit.gxsales.ui.features.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achsanit.gxsales.R
import com.achsanit.gxsales.data.local.entity.ShopItemEntity
import com.achsanit.gxsales.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShopViewModel : ViewModel() {

    private val _shopItemsState =
        MutableStateFlow<Resource<List<ShopItemEntity>>>(Resource.Loading())
    val shopItemState = _shopItemsState.asStateFlow()

    init {
        getShopItems()
    }

    private fun getShopItems() {
        viewModelScope.launch {
            delay(1500)
            _shopItemsState.emit(Resource.Success(listShopItem))
        }
    }

    companion object {
        val listShopItem = listOf(
            ShopItemEntity(
                id = 0,
                title = "A810R AC1200 Router",
                price = "56.000.000",
                stock = 3,
                type = "Onu",
                taxAmount = "56.600",
                taxPercentage = 11,
                image = R.drawable.image_router_1
            ),
            ShopItemEntity(
                id = 1,
                title = "TP-Link AC1200 Router",
                price = "50.000.000",
                stock = 1,
                type = "Onu",
                taxAmount = "50.000",
                taxPercentage = 10,
                image = R.drawable.image_router_3
            ),
            ShopItemEntity(
                id = 2,
                title = "A320R AC3300 Router",
                price = "36.000.000",
                stock = 6,
                type = "Onu",
                taxAmount = "56.600",
                taxPercentage = 11,
                image = R.drawable.image_router_2
            ),
            ShopItemEntity(
                id = 3,
                title = "TAD002 AC300 Router",
                price = "44.000.000",
                stock = 2,
                type = "Onu",
                taxAmount = "44.400",
                taxPercentage = 11,
                image = R.drawable.image_router_4
            ),
        )
    }
}