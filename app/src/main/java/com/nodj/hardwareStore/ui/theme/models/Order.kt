package com.nodj.hardwareStore.ui.theme.models

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.nodj.hardwareStore.R
import java.util.Date

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        )
    ]
)
data class Order(
    @ColumnInfo(name = "order_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "date")
    val date: Date,
    @ColumnInfo(name = "user_id", index = true)
    val userId: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as Product
        if (id != other.id) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        return id ?: -1
    }

    companion object {
        fun getEmpty(): Order {
            return Order(
                id = null,
                date = Date(),
                userId = -1,
            )
        }
    }
}
