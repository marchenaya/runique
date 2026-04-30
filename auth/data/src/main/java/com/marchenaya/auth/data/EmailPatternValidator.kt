package com.marchenaya.auth.data

import android.util.Patterns
import com.marchenaya.auth.domain.PatternValidator

object EmailPatternValidator : PatternValidator {

    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}