package com.rcalencar.weather

import android.content.SharedPreferences
import androidx.core.content.edit
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KProperty

fun Date.isSameDay(other: Date) : Boolean {
    val c1 = Calendar.getInstance()
    val c2 = Calendar.getInstance()
    c1.time = this
    c2.time = other

    return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) && c1.get(Calendar.YEAR) == c2.get(
        Calendar.YEAR)
}

fun <T> SharedPreferences.persist(initial: T, toString: (T) -> String, fromString: (String) -> (T)) : Persistent<T> {
    return Persistent(initial, toString, fromString, this)
}

fun SharedPreferences.persist(initial: String) : Persistent<String> {
    return Persistent(initial, { it }, { it }, this)
}

class Persistent<T>(private val initial: T, private val converterTo: (T) -> String, private val converterFrom: (String) -> (T), private val sharedPreferences: SharedPreferences) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return converterFrom(sharedPreferences.getString("${thisRef?.javaClass?.name}_${property.name}", converterTo(initial))!!)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        sharedPreferences.edit {
            putString("${thisRef?.javaClass?.name}_${property.name}", converterTo(value))
        }
    }
}

fun Date.dateToString(format: String): String {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
    return dateFormatter.format(this)
}