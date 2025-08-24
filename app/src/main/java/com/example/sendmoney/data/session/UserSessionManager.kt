package com.example.sendmoney.data.session


import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userSessionDataStore:
        DataStore<Preferences> by preferencesDataStore(name = "user_session_prefs")

@Singleton
class UserSessionManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val tag = "UserSessionManager"

    companion object {

        val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_user_logged_in")
    }

    val isUserLoggedIn: Flow<Boolean> = context.userSessionDataStore.data
        .catch { exception ->

            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->

            preferences[IS_LOGGED_IN_KEY] ?: false
        }

    suspend fun updateLoginStatus(isLoggedIn: Boolean): Result<Unit> {
        return try {
            context.userSessionDataStore.edit { preferences ->
                preferences[IS_LOGGED_IN_KEY] = isLoggedIn
            }
            Log.d(tag, "Successfully updated login status to: $isLoggedIn")
            Result.success(Unit)
        } catch (exception: IOException) {
            Log.e(tag, "Failed to update login status in DataStore.", exception)
            Result.failure(exception)
        } catch (exception: Exception) {
            Log.e(tag, "An unexpected error occurred while updating login status.",
                exception)
            Result.failure(exception)
        }
    }

    suspend fun checkInitialLoginState(): Boolean {
        return try {
            isUserLoggedIn.first()
        } catch (exception: Exception) {
            false
        }
    }

    suspend fun clearSessionData(): Result<Unit> {
        return try {
            context.userSessionDataStore.edit { preferences ->
                preferences.clear()
            }
            Log.d(tag, "Successfully cleared all session data.")
            Result.success(Unit)
        } catch (exception: IOException) {
            Log.e(tag, "Failed to clear session data from DataStore.", exception)
            Result.failure(exception)
        } catch (exception: Exception) {
            Log.e(tag, "An unexpected error occurred while clearing session data.",
                exception)
            Result.failure(exception)
        }
    }
}

