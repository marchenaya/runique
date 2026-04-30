package com.marchenaya.auth.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}