package com.omteam.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

/**
 * API 호출 처리 후 Flow<Result<Domain>>로 반환
 * 
 * API 응답이 { success, data, error } 구조인 경우 사용
 * 
 * @param logTag 로그에 표시될 태그
 * @param defaultErrorMessage 에러 발생 시 표시할 기본 에러 메시지
 * @param apiCall API 호출하는 suspend fun
 * @param transform 응답 -> 도메인 모델 변환하는 함수
 * @param getErrorInfo 응답에서 에러 정보 추출하는 함수
 * @return Flow<Result<Domain>> 도메인 모델을 감싼 Result Flow
 */
fun <Response, Domain> safeApiCall(
    logTag: String,
    defaultErrorMessage: String = "알 수 없는 에러",
    apiCall: suspend () -> Response,
    transform: (Response) -> Domain?,
    getErrorInfo: (Response) -> ErrorInfo
): Flow<Result<Domain>> = flow {
    try {
        Timber.d("## $logTag 시작")

        val response = apiCall()
        val domain = transform(response)

        if (domain != null) {
            emit(Result.success(domain))
        } else {
            val errorInfo = getErrorInfo(response)
            val errorMessage = errorInfo.message ?: defaultErrorMessage
            emit(Result.failure(Exception(errorMessage)))
        }
    } catch (e: Exception) {
        emit(Result.failure(e))
    }
}

data class ErrorInfo(
    val code: String?,
    val message: String?
)