package com.bltech.moxtel.global.util

val String?.unwrapped: String
    get() = this ?: ""
