package com.boysatria.rekomendasifilm.utils

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.SocketTimeoutException

class ErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        try {
            proceed(
                request()
                    .newBuilder()
                    .build()
            )
        } catch (e: Exception) {
            var msg = ""
            var errorCode: Int = 909

            when (e) {
                is SocketTimeoutException -> {
                    msg = e.message?:""
                    errorCode = 909
                }


            }

            val original = chain.request()

            val request = original.newBuilder()
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/json")
                .removeHeader("Pragma")
                .build()

            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(errorCode)
                .message(msg)
                .body("{${e}}".toResponseBody(null)).build()

        }
    }
}