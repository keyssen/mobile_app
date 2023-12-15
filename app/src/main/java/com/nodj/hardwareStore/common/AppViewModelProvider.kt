package com.nodj.hardwareStore.common

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nodj.hardwareStore.ui.ShopApplication
import com.nodj.hardwareStore.ui.authenticator.AuthenticatorViewModel
import com.nodj.hardwareStore.ui.page.cart.ProductListInCartViewModel
import com.nodj.hardwareStore.ui.page.category.edit.CategoryEditViewModel
import com.nodj.hardwareStore.ui.page.category.list.CategoryListViewModel
import com.nodj.hardwareStore.ui.page.orders.OrdersViewModel
import com.nodj.hardwareStore.ui.page.orders.orderContent.OrderViewModel
import com.nodj.hardwareStore.ui.page.product.ProductViewModel
import com.nodj.hardwareStore.ui.page.product.edit.CategoryDropDownViewModel
import com.nodj.hardwareStore.ui.page.product.edit.ProductEditViewModel
import com.nodj.hardwareStore.ui.page.product.list.ProductListViewModel
import com.nodj.hardwareStore.ui.page.profile.UserViewModel
import com.nodj.hardwareStore.ui.page.report.ReportViewModel
import com.nodj.hardwareStore.ui.page.signIn.SignInViewModel
import com.nodj.hardwareStore.ui.page.signUp.SignUpViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ProductListViewModel(
                shopApplication().container.restProductRepository,
                shopApplication().container.restUserWithProductsRepository,
            )
        }
        initializer {
            ProductListInCartViewModel(
                shopApplication().container.restProductRepository,
                shopApplication().container.restUserWithProductsRepository,
                shopApplication().container.restOrderWithProductsRepository,
                shopApplication().container.restOrderRepository,
            )
        }
        initializer {
            ProductEditViewModel(
                this.createSavedStateHandle(),
                shopApplication().container.restProductRepository
            )
        }
        initializer {
            ProductViewModel(
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
            SignInViewModel(
                shopApplication().container.restUserRepository
            )
        }
        initializer {
            SignUpViewModel(
                shopApplication().container.restUserRepository
            )
        }
        initializer {
            AuthenticatorViewModel(
                shopApplication().container.restUserRepository,
            )
        }
        initializer {
            ReportViewModel(
                shopApplication().container.service,
            )
        }
    }
}

fun CreationExtras.shopApplication(): ShopApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ShopApplication)