package com.omteam.main

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import java.security.MessageDigest
import java.util.UUID

/**
 * 구글 로그인 매니저
 *
 * Credential Manager API를 사용한 구글 로그인 처리 담당
 */
object GoogleLoginManager {
    private const val TAG = "GoogleLoginManager"
    
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
        Log.d(TAG, "signIn() 시작")
        Log.d(TAG, "Web Client ID: ${webClientId.take(20)}...")
        
        return try {
            // Nonce 생성 (보안을 위한 랜덤 문자열)
            val rawNonce = UUID.randomUUID().toString()
            val bytes = rawNonce.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }
            
            Log.d(TAG, "Nonce 생성 완료")
            
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)  // 모든 계정 표시
                .setServerClientId(webClientId)
                .setNonce(hashedNonce)
                .build()
            
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            
            Log.d(TAG, "Credential Manager로 로그인 요청 중...")
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            
            Log.d(TAG, "Credential Manager 응답 받음")
            handleSignInResult(result)
        } catch (e: GetCredentialException) {
            Log.e(TAG, "GetCredentialException 발생: ${e.message}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "예외 발생: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * 로그인 결과 처리
     */
    private fun handleSignInResult(result: GetCredentialResponse): Result<GoogleIdTokenCredential> {
        Log.d(TAG, "handleSignInResult() 시작")
        
        return when (val credential = result.credential) {
            is CustomCredential -> {
                Log.d(TAG, "CustomCredential 타입: ${credential.type}")
                
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        Log.d(TAG, "구글 로그인 성공: ${googleIdTokenCredential.id}")
                        Log.d(TAG, "이름: ${googleIdTokenCredential.displayName}")
                        Result.success(googleIdTokenCredential)
                    } catch (e: Exception) {
                        Log.e(TAG, "GoogleIdTokenCredential 생성 실패: ${e.message}", e)
                        Result.failure(e)
                    }
                } else {
                    Log.e(TAG, "예상치 못한 Credential 타입: ${credential.type}")
                    Result.failure(Exception("Unexpected credential type: ${credential.type}"))
                }
            }
            else -> {
                Log.e(TAG, "CustomCredential이 아닌 타입")
                Result.failure(Exception("Unexpected credential type"))
            }
        }
    }
}
