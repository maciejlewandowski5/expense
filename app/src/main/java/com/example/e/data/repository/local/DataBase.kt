package com.example.e.data.repository.local

import androidx.room.*
import com.example.e.data.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Database(
    entities = [ExpenseModel::class, AccountingGroupModel::class, ParticipantModel::class, UserModel::class, MemberModel::class],
    version = 1, exportSchema = true,
)
@TypeConverters(LocalDateTimeConverter::class, BigDecimalTypeConverter::class)
abstract class DataBase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getGroupDao(): GroupDao
    abstract fun getParticipantDao(): ParticipantDao
    abstract fun getMemberDao(): MemberDao
    abstract fun getExpenseDao(): ExpenseDao
}

class LocalDateTimeConverter {
    @TypeConverter
    fun toDate(dateString: String?): LocalDateTime? = dateString?.let { LocalDateTime.parse(it) }

    @TypeConverter
    fun toDateString(date: LocalDateTime?): String? = date?.toString()
}

class BigDecimalTypeConverter {
    @TypeConverter
    fun bigDecimalToString(input: BigDecimal?): String = input?.toPlainString() ?: ""

    @TypeConverter
    fun stringToBigDecimal(input: String?): BigDecimal =
        if (input.isNullOrBlank()) BigDecimal.valueOf(0.0) else
            input.toBigDecimalOrNull() ?: BigDecimal.valueOf(0.0)
}
