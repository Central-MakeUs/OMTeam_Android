package com.omteam.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

/**
 * API 에러 정보의 공통 인터페이스
 */
interface ErrorInfo {
    val code: String
    val message: String
}

/**
 * API 호출 결과를 나타내는 공통 구조
 * 
 * 모든 API 응답이 { success, data, error } 형태를 따름
 */
data class ApiResult<T, E : ErrorInfo?>(
    val success: Boolean,
    val data: T?,
    val error: E
)

/**
 * API 호출을 안전하게 처리하고 Flow<Result<Domain>>로 반환
 * 
 * 서버 API 응답이 { success, data, error } 구조를 따르는 경우 사용
 * 
 * @param logTag 로그에 표시될 태그 (예: "일일 미션 상태 조회")
 * @param defaultErrorMessage 에러 발생 시 기본 에러 메시지
 * @param apiCall API 호출 및 ApiResult 변환 람다
 * @param mapper 응답 데이터를 도메인 모델로 변환하는 함수
 * @return Flow<Result<Domain>> 도메인 모델을 감싼 Result Flow
 */
fun <Data, Domain, E : ErrorInfo?> safeApiCall(
    logTag: String,
    defaultErrorMessage: String,
    apiCall: suspend () -> ApiResult<Data, E>,
    mapper: (Data) -> Domain
): Flow<Result<Domain>> = flow {
    try {
        Timber.d("## $logTag 시작")
        
        val result = apiCall()
        val data = result.data
        
        if (result.success && data != null) {
            Timber.d("## $logTag 성공")
            emit(Result.success(mapper(data)))
        } else {
            val errorMessage = result.error?.message ?: defaultErrorMessage
            val errorCode = result.error?.code
            Timber.e("## $logTag 실패 - $errorCode: $errorMessage")
            emit(Result.failure(Exception(errorMessage)))
        }
    } catch (e: Exception) {
        Timber.e(e, "## $logTag 예외 발생")
        emit(Result.failure(e))
    }
}

/**
 * Response를 ApiResult로 변환하는 헬퍼 함수
 * 
 * 모든 API Response 타입에서 공통적으로 사용 가능
 */
fun <T, E : ErrorInfo?> toApiResult(success: Boolean, data: T?, error: E): ApiResult<T, E> =
    ApiResult(success, data, error)
