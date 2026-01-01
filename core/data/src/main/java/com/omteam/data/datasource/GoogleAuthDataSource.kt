package com.omteam.data.datasource

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.omteam.domain.model.UserInfo
import javax.inject.Inject

/**
 * 구글 로그인 datasource 구현체
 *
 * Credential Manager API 통해 유저 정보 조회, 로그아웃 처리
 */
class GoogleAuthDataSource @Inject constructor(
    private val context: Context,
    private val credentialManager: CredentialManager,
    private val webClientId: String
) : AuthDataSource {

    companion object {
        private const val TAG = "GoogleAuthDataSource"
    }
    
    override suspend fun getUserInfo(): Result<UserInfo> {
        Log.d(TAG, "getUserInfo() 시작")
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true) // 이미 로그인된 계정만
                .setServerClientId(webClientId)
                .build()
            
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            
            Log.d(TAG, "Credential Manager에 사용자 정보 요청 중...")
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            
            when (val credential = result.credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val userInfo = UserInfo(
                            id = googleIdTokenCredential.id.hashCode().toLong(),
                            nickname = googleIdTokenCredential.displayName,
                            email = googleIdTokenCredential.id
                        )
                        Log.d(TAG, "사용자 정보 조회 성공 : ${userInfo.email}")
                        Result.success(userInfo)
                    } else {
                        Log.e(TAG, "예상 못한 Credential 타입 : ${credential.type}")
                        Result.failure(Exception("Unexpected credential type"))
                    }
                }
                else -> {
                    Log.e(TAG, "CustomCredential이 아닌 타입")
                    Result.failure(Exception("Unexpected credential type"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "사용자 정보 조회 실패 : ${e.message}", e)
            Result.failure(e)
        }
    }
    
    override suspend fun logout(): Result<Unit> {
        return try {
            credentialManager.clearCredentialState(
                ClearCredentialStateRequest()
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
