package com.omteam.network.interceptor

import com.google.firebase.crashlytics.FirebaseCrashlytics
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

@Singleton
class CrashlyticsNetworkLoggingInterceptor @Inject constructor() : Interceptor {

    companion object {
        private const val MAX_BODY_PREVIEW_BYTES: Long = 8_192L
        private const val MAX_LOG_LENGTH = 1_000
        private const val REDACTED_VALUE = "***"
        private const val MAX_AUTH_RETRY_COUNT = 3
    }

    private val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = readRequestBody(request)
        val requestLog = buildString {
            append("API 요청")
            append("\n- 메서드 : ${request.method}")
            append("\n- url : ${request.url}")
            append("\n- 헤더 : ${sanitizeHeaders(request)}")
            if (requestBody != null) {
                append("\n- 요청 본문 : $requestBody")
            }
        }
        crashlytics.log(limitLength(requestLog))

        return try {
            val response = chain.proceed(request)
            val responseBody = response.peekBody(MAX_BODY_PREVIEW_BYTES).string()
            val responseLog = buildString {
                append("API 응답")
                append("\n- 상태 코드 : ${response.code}")
                append("\n- 메시지 : ${response.message}")
                append("\n- url : ${request.url}")
                append("\n- 응답 : $responseBody")
            }
            crashlytics.log(limitLength(responseLog))
            if (shouldRecordHttpException(request, response)) {
                crashlytics.recordException(buildHttpStatusException(request, response))
            }
            response
        } catch (throwable: Throwable) {
            crashlytics.log(
                limitLength(
                    "API 실패\n- 메서드 : ${request.method}\n- url : ${request.url}\n- 원인 : ${throwable.message}"
                )
            )
            if (throwable is IOException) {
                crashlytics.recordException(throwable)
            }
            throw throwable
        }
    }

    private fun sanitizeHeaders(request: Request): String {
        val redacted = request.headers.newBuilder()
            .set("Authorization", REDACTED_VALUE)
            .set("Cookie", REDACTED_VALUE)
            .set("Set-Cookie", REDACTED_VALUE)
            .build()

        return redacted.toString().trim()
    }

    private fun readRequestBody(request: Request): String? {
        val body = request.body ?: return null
        return runCatching {
            val buffer = Buffer()
            body.writeTo(buffer)
            val charset = body.contentType()?.charset(Charset.forName("UTF-8")) ?: Charsets.UTF_8
            buffer.readString(charset)
        }.getOrElse { "unreadable body (${it::class.java.simpleName})" }
    }

    private fun limitLength(message: String): String {
        if (message.length <= MAX_LOG_LENGTH) {
            return message
        }
        val truncatedLength = min(MAX_LOG_LENGTH, message.length)

        return message.substring(0, truncatedLength)
    }

    private fun shouldRecordHttpException(request: Request, response: Response): Boolean {
        val statusCode = response.code
        if (statusCode in 500..599) {
            return true
        }

        return when (statusCode) {
            429 -> true
            401 -> getRetryCount(request) >= MAX_AUTH_RETRY_COUNT
            else -> false
        }
    }

    private fun buildHttpStatusException(request: Request, response: Response): Throwable {
        val retryCount = getRetryCount(request)
        val message = buildString {
            append("HTTP 오류")
            append("\n- 메서드 : ${request.method}")
            append("\n- url : ${request.url}")
            append("\n- 상태 코드 : ${response.code}")
            append("\n- 메시지 : ${response.message}")
            append("\n- 재시도 횟수 : $retryCount")
        }

        return IllegalStateException(limitLength(message))
    }

    private fun getRetryCount(request: Request): Int =
        request.header("X-Retry-Count")?.toIntOrNull() ?: 0
}