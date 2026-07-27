package com.marchenaya.core.data.networking.httpclient

object SensitiveDataRedactor {

    private const val PLACEHOLDER = "***"

    private val sensitiveKeys = listOf("password", "accessToken", "refreshToken")

    private val patterns = sensitiveKeys.map { key ->
        Regex("(\"$key\"\\s*:\\s*\")(.*?)(\")")
    }

    fun redact(message: String): String =
        patterns.fold(message) { acc, regex ->
            regex.replace(acc) { match ->
                "${match.groupValues[1]}$PLACEHOLDER${match.groupValues[3]}"
            }
        }
}