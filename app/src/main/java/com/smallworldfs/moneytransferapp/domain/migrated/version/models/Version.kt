package com.smallworldfs.moneytransferapp.domain.migrated.version.models

import kotlin.math.max

class Version(val name: String) : Comparable<Version> {

    override fun compareTo(that: Version): Int {
        if (that == null) return 1
        val thisParts: List<String> = this.name.split(".")
        val thatParts: List<String> = that.name.split(".")
        val length = max(thisParts.size, thatParts.size)
        for (i in 0 until length) {
            val thisPart = if (i < thisParts.size) thisParts[i].toInt() else 0
            val thatPart = if (i < thatParts.size) thatParts[i].toInt() else 0
            if (thisPart < thatPart) return -1
            if (thisPart > thatPart) return 1
        }
        return 0
    }

    override fun equals(that: Any?): Boolean {
        if (this === that) return true
        if (that == null) return false
        return if (this.javaClass != that.javaClass) false else this.compareTo(that as Version) == 0
    }
}
