package com.example.e.login.session

import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

@ExperimentalSerializationApi
class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        if (isSecuredPath(chain)) {
            putAuthHeader(chain)
        } else {
            chain.proceed(chain.request())
        }

    private fun putAuthHeader(chain: Interceptor.Chain) =
        tokenRepository.accessToken.value?.token?.let { token ->
            chain.proceed(
                chain.request().newBuilder().also {
                    it.addHeader(AUTH_HEADER, "$BEARER $token")
                }.build()
            )
        } ?: dummyResponse(chain)

    private fun dummyResponse(chain: Interceptor.Chain) = Response.Builder().code(600)
        .message("Can not send request to secured path without token").request(chain.request())
        .protocol(Protocol.HTTP_2)
        .body("".toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())).build()

    private fun isSecuredPath(chain: Interceptor.Chain) =
        chain.request().url.encodedPath.startsWith(SECURED_PATH_PREFIX)

    companion object {
        private const val AUTH_HEADER = "Authorization"
        private const val BEARER = "Bearer"
        private const val SECURED_PATH_PREFIX = "/s"
    }
}
