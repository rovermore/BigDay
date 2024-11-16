package com.smallworldfs.moneytransferapp.base.domain.utils

import java.util.TreeMap

fun <K, V : Comparable<V>> Map<K, V>.sortByValue(): TreeMap<K, V> {
    val valueComparator = object : Comparator<K> {
        override fun compare(k1: K, k2: K): Int {
            val compare = this@sortByValue.getValue(k1).compareTo(this@sortByValue.getValue(k2))
            return if (compare == 0)
                1
            else
                compare
        }
    }

    val sortedByValues = TreeMap<K, V>(valueComparator)
    sortedByValues.putAll(this)
    return sortedByValues
}
