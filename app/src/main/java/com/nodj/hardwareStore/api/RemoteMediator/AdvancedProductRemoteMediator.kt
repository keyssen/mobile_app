package com.nodj.hardwareStore.api.RemoteMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.nodj.hardwareStore.api.MyServerService
import com.nodj.hardwareStore.api.model.helperModel.toAdvancedProduct
import com.nodj.hardwareStore.api.model.toProduct
import com.nodj.hardwareStore.db.database.AppDatabase
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.models.manyToMany.UserWithProducts
import com.nodj.hardwareStore.db.remotekeys.dao.OfflineRemoteKeyRepository
import com.nodj.hardwareStore.db.remotekeys.dao.RemoteKeyType
import com.nodj.hardwareStore.db.remotekeys.dao.RemoteKeys
import com.nodj.hardwareStore.db.repository.IncompleteOfflineProductRepository
import com.nodj.hardwareStore.db.repository.OfflineUserWithProductsRepository
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class AdvancedProductRemoteMediator(
    private val service: MyServerService,
    private val dbProductRepository: IncompleteOfflineProductRepository,
    private val dbUserWithProductsRepository: OfflineUserWithProductsRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase,
    private val userId: Int
) : RemoteMediator<Int, AdvancedProduct>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AdvancedProduct>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val products =
                service.getProducts(page, state.config.pageSize).map { it.toProduct() }
            val advancedProducts =
                service.getByUserAdvancedProducts(page, state.config.pageSize, userId, "product")
                    .map { it.toAdvancedProduct() }
            val userWithProducts =
                advancedProducts.map { UserWithProducts(userId, it.product.id, it.count) }
            val endOfPaginationReached = advancedProducts.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dbRemoteKeyRepository.deleteRemoteKey(RemoteKeyType.ADVANCEDPRODUCT)
                    dbUserWithProductsRepository.deleteAll()
                    dbProductRepository.deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = advancedProducts.map {
                    RemoteKeys(
                        entityId = it.product.id,
                        type = RemoteKeyType.ADVANCEDPRODUCT,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                dbRemoteKeyRepository.createRemoteKeys(keys)
                dbUserWithProductsRepository.getAll()
                dbUserWithProductsRepository.insertUserWithProducts(userWithProducts)
                dbProductRepository.insertProducts(products)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, AdvancedProduct>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { advancedProduct ->
                dbRemoteKeyRepository.getAllRemoteKeys(
                    advancedProduct.product.id,
                    RemoteKeyType.ADVANCEDPRODUCT
                )
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, AdvancedProduct>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { advancedProduct ->
                dbRemoteKeyRepository.getAllRemoteKeys(
                    advancedProduct.product.id,
                    RemoteKeyType.ADVANCEDPRODUCT
                )
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, AdvancedProduct>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.product?.id?.let { productUid ->
                dbRemoteKeyRepository.getAllRemoteKeys(productUid, RemoteKeyType.ADVANCEDPRODUCT)
            }
        }
    }
}