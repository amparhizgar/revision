package com.amirhparhizgar.revision.service

import java.util.*

object Util {
    fun uuid(): Int {
        val idOne: UUID = UUID.randomUUID()
        var str = "" + idOne
        val uid = str.hashCode()
        val filterStr = "" + uid
        str = filterStr.replace("-".toRegex(), "")
        return str.toInt()
    }
}