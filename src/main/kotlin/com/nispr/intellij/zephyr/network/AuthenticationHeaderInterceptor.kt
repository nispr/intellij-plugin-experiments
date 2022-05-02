package com.nispr.intellij.zephyr.network

import com.nispr.intellij.zephyr.settings.CredentialRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*

class AuthenticationHeaderInterceptor(private val credentialRepository: CredentialRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .addAuthHeader()
                .build()
        )
    }

    private fun Request.Builder.addAuthHeader(): Request.Builder {
        return authToken?.let {
            addHeader("Authorization", "Basic ")
        } ?: this
    }

    private val authToken
        get() = credentialRepository.get()?.let {
            Base64.getEncoder().encode("${it.userName}:${it.password}".toByteArray()).toString(Charsets.UTF_8)
        }
}