package com.fomaxtro.notemark.domain.validator

interface PatternMatching {
    fun isEmail(email: String): Boolean
}