package com.nodj.hardwareStore.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.nodj.hardwareStore.api.model.CategoryRemote
import com.nodj.hardwareStore.api.model.OrderRemote
import com.nodj.hardwareStore.api.model.ProductRemote
import com.nodj.hardwareStore.api.model.UserRemote
import com.nodj.hardwareStore.api.model.helperModel.AdvancedProductRemote
import com.nodj.hardwareStore.api.model.helperModel.ProductFromOrderRemote
import com.nodj.hardwareStore.api.model.manyToMany.OrderWithProductsRemote
import com.nodj.hardwareStore.api.model.manyToMany.UserWithProductsRemote
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface MyServerService {
    @GET("categories")
    suspend fun getСategories(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int,
    ): List<CategoryRemote>

    @GET("categories")
    suspend fun getAllСategories(): List<CategoryRemote>

    @GET("categories/{id}")
    suspend fun getCategory(
        @Path("id") id: Int,
    ): CategoryRemote

    @POST("categories")
    suspend fun createCategory(
        @Body category: CategoryRemote,
    ): CategoryRemote

    @PUT("categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Int,
        @Body category: CategoryRemote,
    ): CategoryRemote

    @DELETE("categories/{id}")
    suspend fun deleteCategory(
        @Path("id") id: Int,
    ): CategoryRemote

    @GET("users")
    suspend fun getUsers(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int,
    ): List<UserRemote>

    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: Int,
    ): UserRemote

    @POST("users")
    suspend fun createUser(
        @Body user: UserRemote,
    ): UserRemote

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body user: UserRemote,
    ): UserRemote

    @DELETE("users/{id}")
    suspend fun deleteUser(
        @Path("id") id: Int,
    ): UserRemote

    @GET("products")
    suspend fun getProducts(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int,
    ): List<ProductRemote>

    @GET("products")
    suspend fun getByCategoryProducts(
        @Query("categoryId") categoryId: Int,
    ): List<ProductRemote>

//    @GET("products")
//    suspend fun getByUserProducts(
//        @Query("userId") userId: Int,
//    ): List<ProductRemote>

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: Int,
    ): ProductRemote

    @POST("products")
    suspend fun createProduct(
        @Body product: ProductRemote,
    ): ProductRemote

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: ProductRemote,
    ): ProductRemote

    @DELETE("products/{id}")
    suspend fun deleteProduct(
        @Path("id") id: Int,
    ): ProductRemote

    @GET("user_with_products")
    suspend fun getAllUserWithProduct(): List<UserWithProductsRemote>

    @GET("user_with_products")
    suspend fun getByUserProduct(
        @Query("userId") userId: Int,
        @Query("productId") productId: Int,
    ): List<UserWithProductsRemote>

    @GET("user_with_products")
    suspend fun getByUserUserWithProducts(
        @Query("userId") userId: Int,
    ): List<UserWithProductsRemote>

    @GET("user_with_products")
    suspend fun getAllByUserAdvancedProducts(
        @Query("userId") userId: Int,
        @Query("_expand") _expand: String,
    ): List<AdvancedProductRemote>

    @GET("user_with_products")
    suspend fun getByUserAdvancedProducts(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int,
        @Query("userId") userId: Int,
        @Query("_expand") _expand: String,
    ): List<AdvancedProductRemote>

    @POST("user_with_products")
    suspend fun createUserWithProduct(
        @Body product: UserWithProductsRemote,
    ): UserWithProductsRemote

    @PUT("user_with_products/{id}")
    suspend fun updateUserWithProduct(
        @Path("id") id: Int,
        @Body userWithProductsRemote: UserWithProductsRemote,
    ): UserWithProductsRemote

    @DELETE("user_with_products/{id}")
    suspend fun deleteUserWithProduct(
        @Path("id") id: Int,
    ): UserWithProductsRemote

    @POST("orders")
    suspend fun createOrder(
        @Body order: OrderRemote,
    ): OrderRemote

    @POST("order_with_products")
    suspend fun createOrderWithProduct(
        @Body orderWithProductsRemote: OrderWithProductsRemote,
    ): OrderWithProductsRemote

    @GET("order_with_products")
    suspend fun getProductFromOrdersByOrder(
        @Query("orderId") orderId: Int,
        @Query("_expand") _expand: String,
    ): List<ProductFromOrderRemote>

    @GET("order_with_products")
    suspend fun getProductFromOrdersByUser(
        @Query("userId") userId: Int,
        @Query("_expand") _expand: String,
    ): List<ProductFromOrderRemote>

    @GET("orders")
    suspend fun getOrdersByUser(
        @Query("userId") userId: Int,
    ): List<OrderRemote>

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8079/"

        @Volatile
        private var INSTANCE: MyServerService? = null

        fun getInstance(): MyServerService {
            return INSTANCE ?: synchronized(this) {
                val json = Json {
                    ignoreUnknownKeys = true
                }
                val logger = HttpLoggingInterceptor()
                logger.level = HttpLoggingInterceptor.Level.BASIC
                val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                    .build()
                    .create(MyServerService::class.java)
                    .also { INSTANCE = it }
            }
        }
    }
}



