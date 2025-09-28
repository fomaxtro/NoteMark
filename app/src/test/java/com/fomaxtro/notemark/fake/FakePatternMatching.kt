package com.fomaxtro.notemark.fake

import com.fomaxtro.notemark.domain.validator.PatternMatching

class FakePatternMatching : PatternMatching {
    var isEmailValid = true

    override fun isEmail(email: String): Boolean {
        return isEmailValid
    }
}