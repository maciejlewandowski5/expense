package com.example.e

import android.util.Log
import com.example.e.domain.GroupId
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

fun Long.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(this), TimeZone.getDefault().toZoneId())

fun Throwable.logThrowable(loggerOwner: Any) =
    this.message?.let { Log.e(loggerOwner::class.simpleName, it) }

fun BigDecimal.changeSign(): BigDecimal = multiply(MINUS_ONE)

val MINUS_ONE = BigDecimal("-1")
val ACCURACY = BigDecimal("0.02")
const val DEFAULT_CURRENCY = "PLN"
const val DEFAULT_ID_FOR_AUTOGENERATE = 0L
val DEFAULT_AUTOGENERATE_GROUP_ID = GroupId(0L)
const val BIG_DECIMAL_SCALE = 2
