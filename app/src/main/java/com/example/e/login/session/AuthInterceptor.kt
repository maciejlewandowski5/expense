package com.example.e.login.session

import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.*
import javax.inject.Inject

@ExperimentalSerializationApi
class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = if (isSecuredPath(chain)) {
        putAuthHeader(chain)
    } else {
        chain.proceed(chain.request())
    }

    private fun putAuthHeader(chain: Interceptor.Chain) = chain.proceed(
        chain.request().newBuilder().also {
            it.addHeader(AUTH_HEADER, "$BEARER ${tokenRepository.accessToken.value?.token}")
        }.build()
    )

    private fun isSecuredPath(chain: Interceptor.Chain) =
        chain.request().url.encodedPath.startsWith(SECURED_PATH_PREFIX)

    companion object {
        private const val AUTH_HEADER = "Authorization"
        private const val BEARER = "Bearer"
        private const val SECURED_PATH_PREFIX = "/s"
    }
}
