package com.nodj.hardwareStore.common

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nodj.hardwareStore.ui.ShopApplication
import com.nodj.hardwareStore.ui.category.CategorizedProductsViewModel
import com.nodj.hardwareStore.ui.category.CategoryListViewModel
import com.nodj.hardwareStore.ui.category.edit.CategoryEditViewModel
import com.nodj.hardwareStore.ui.page.cart.ProductListInCartViewModel
import com.nodj.hardwareStore.ui.page.orders.OrdersViewModel
import com.nodj.hardwareStore.ui.page.orders.order.OrderViewModel
import com.nodj.hardwareStore.ui.page.profile.UserViewModel
import com.nodj.hardwareStore.ui.product.edit.CategoryDropDownViewModel
import com.nodj.hardwareStore.ui.product.edit.ProductEditViewModel
import com.nodj.hardwareStore.ui.product.list.ProductListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ProductListViewModel(
                shopApplication().container.restProductRepository,
                shopApplication().container.restUserWithProductsRepository
            )
        }
        initializer {
            ProductListInCartViewModel(
                shopApplication().container.restProductRepository,
                shopApplication().container.restUserWithProductsRepository,
                shopApplication().container.restOrderWithProductsRepository,
                shopApplication().container.restOrderRepository,
//                OrderService(
//                    shopApplication().container.orderRepository,
//                    shopApplication().container.productRepository,
//                    shopApplication().container.orderWithProductsRepository,
//                    shopApplication().container.userWithProductsRepository,
//                )
            )
        }
        initializer {
            ProductEditViewModel(
                this.createSavedStateHandle(),
                shopApplication().container.restProductRepository
            )
        }
        initializer {
            CategoryDropDownViewModel(shopApplication().container.restCategoryRepository)
        }
        initializer {
            OrdersViewModel(
                shopApplication().container.restOrderRepository
            )
        }
        initializer {
            OrderViewModel(
                this.createSavedStateHandle(),
                shopApplication().container.restProductRepository,
                shopApplication().container.restOrderRepository,
                shopApplication().container.restUserWithProductsRepository,
            )
        }
        initializer {
            CategoryListViewModel(
                shopApplication().container.restCategoryRepository
            )
        }
        initializer {
            CategoryEditViewModel(
                this.createSavedStateHandle(),
                shopApplication().container.restCategoryRepository
            )
        }
        initializer {
            UserViewModel(
                shopApplication().container.restUserRepository
            )
        }
        initializer {
            CategorizedProductsViewModel(
                shopApplication().container.restProductRepository,
                shopApplication().container.restUserWithProductsRepository,
                this.createSavedStateHandle()
            )
        }
    }
}

fun CreationExtras.shopApplication(): ShopApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ShopApplication)