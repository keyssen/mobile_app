package com.nodj.hardwareStore.api.RemoteMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.nodj.hardwareStore.api.MyServerService
import com.nodj.hardwareStore.api.model.toProduct
import com.nodj.hardwareStore.db.database.AppDatabase
import com.nodj.hardwareStore.db.models.helperModels.AdvancedProduct
import com.nodj.hardwareStore.db.remotekeys.dao.OfflineRemoteKeyRepository
import com.nodj.hardwareStore.db.remotekeys.dao.RemoteKeyType
import com.nodj.hardwareStore.db.remotekeys.dao.RemoteKeys
import com.nodj.hardwareStore.db.repository.OfflineProductRepository
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator(
    private val service: MyServerService,
    private val dbProductRepository: OfflineProductRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase
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
            val endOfPaginationReached = products.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dbRemoteKeyRepository.deleteRemoteKey(RemoteKeyType.PRODUCT)
                    dbProductRepository.deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = products.map {
                    RemoteKeys(
                        entityId = it.id,
                        type = RemoteKeyType.PRODUCT,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                dbRemoteKeyRepository.createRemoteKeys(keys)
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
            ?.let { product ->
                dbRemoteKeyRepository.getAllRemoteKeys(product.product.id, RemoteKeyType.PRODUCT)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, AdvancedProduct>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { product ->
                dbRemoteKeyRepository.getAllRemoteKeys(product.product.id, RemoteKeyType.PRODUCT)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, AdvancedProduct>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.product?.id?.let { productUid ->
                dbRemoteKeyRepository.getAllRemoteKeys(productUid, RemoteKeyType.PRODUCT)
            }
        }
    }
}