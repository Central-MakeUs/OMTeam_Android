package com.omteam.main

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import timber.log.Timber

/**
 * 구글 로그인 매니저
 *
 * Credential Manager API를 사용한 구글 로그인 처리 담당
 */
object GoogleLoginManager {
    
    /**
     * 구글 로그인 요청
     * @param context Context
     * @param credentialManager CredentialManager 인스턴스
     * @param webClientId Firebase Console에서 가져온 Web Client ID
     * @return GetCredentialRequest
     */
    suspend fun signIn(
        context: Context,
        credentialManager: CredentialManager,
        webClientId: String
    ): Result<GoogleIdTokenCredential> {
        Timber.d( "구글 로그인 - signIn() 시작")
        Timber.d( "Web Client ID: ${webClientId.take(20)}...")

        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false) // 모든 계정 표시
                .setServerClientId(webClientId)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            Timber.d( "Credential Manager로 로그인 요청 중...")
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            Timber.d( "Credential Manager 응답 받음")
            handleSignInResult(result)
        } catch (e: GetCredentialException) {
            Timber.e(e, "GetCredentialException 발생 : ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Timber.e(e, "예외 발생 : ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * 로그인 결과 처리
     */
    private fun handleSignInResult(result: GetCredentialResponse): Result<GoogleIdTokenCredential> {
        Timber.d( "handleSignInResult() 시작")

        return when (val credential = result.credential) {
            is CustomCredential -> {
                Timber.d( "CustomCredential 타입 : ${credential.type}")

                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        Timber.d( "구글 로그인 성공 : ${googleIdTokenCredential.id}")
                        Timber.d( "이름 : ${googleIdTokenCredential.displayName}")
                        Result.success(googleIdTokenCredential)
                    } catch (e: Exception) {
                        Timber.e(e, "GoogleIdTokenCredential 생성 실패 : ${e.message}")
                        Result.failure(e)
                    }
                } else {
                    Timber.e( "예상치 못한 Credential 타입 : ${credential.type}")
                    Result.failure(Exception("Unexpected credential type : ${credential.type}"))
                }
            }
            else -> {
                Timber.e( "CustomCredential이 아닌 타입")
                Result.failure(Exception("Unexpected credential type"))
            }
        }
    }
}