package com.nodj.hardwareStore.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nodj.hardwareStore.db.database.ShopApplication
import com.nodj.hardwareStore.db.service.OrderService
import com.nodj.hardwareStore.ui.category.CategorizedProductsViewModel
import com.nodj.hardwareStore.ui.category.CategoryListViewModel
import com.nodj.hardwareStore.ui.category.edit.CategoryEditViewModel
import com.nodj.hardwareStore.ui.page.cart.ProductListInCartViewModel
import com.nodj.hardwareStore.ui.page.orders.OrdersViewModel
import com.nodj.hardwareStore.ui.page.orders.order.OrderViewModel
import com.nodj.hardwareStore.ui.product.edit.CategoryDropDownViewModel
import com.nodj.hardwareStore.ui.product.edit.ProductEditViewModel
import com.nodj.hardwareStore.ui.product.list.ProductListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ProductListViewModel(
                shopApplication().container.productRepository,
                shopApplication().container.userWithProductsRepository
            )
        }
        initializer {
            ProductListInCartViewModel(
                shopApplication().container.productRepository,
                shopApplication().container.userWithProductsRepository,
                OrderService(
                    shopApplication().container.orderRepository,
                    shopApplication().container.productRepository,
                    shopApplication().container.orderWithProductsRepository,
                    shopApplication().container.userWithProductsRepository,
                ))
        }
        initializer {
            ProductEditViewModel(
                this.createSavedStateHandle(),
                shopApplication().container.productRepository
            )
        }
        initializer {
            CategoryDropDownViewModel(shopApplication().container.categoryRepository)
        }
        initializer {
            OrdersViewModel(
                shopApplication().container.orderRepository
            )
        }
        initializer {
            OrderViewModel(
                this.createSavedStateHandle(),
                shopApplication().container.productRepository,
                shopApplication().container.orderRepository,
                shopApplication().container.userWithProductsRepository,
            )
        }
        initializer {
            CategoryListViewModel(
                shopApplication().container.categoryRepository
            )
        }
        initializer {
            CategorizedProductsViewModel(
                shopApplication().container.productRepository,
                shopApplication().container.userWithProductsRepository,
                this.createSavedStateHandle()
            )
        }
        initializer {
            CategoryEditViewModel(
                this.createSavedStateHandle(),
                shopApplication().container.categoryRepository
            )
        }
    }
}

fun CreationExtras.shopApplication(): ShopApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ShopApplication)