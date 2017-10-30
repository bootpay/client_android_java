package kr.co.bootpay.pref

import kotlin.reflect.KProperty

interface ReadOnlyProperty<in R, out T> {
    operator fun getValue(thisRef: R, property: KProperty<*>): T
}
