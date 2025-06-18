package com.fomaxtro.notemark.data.validator

import android.util.Patterns
import com.fomaxtro.notemark.domain.validator.PatternMatching

class AndroidPatternMatching : PatternMatching {
    override fun isEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}