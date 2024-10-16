package com.example.tasky.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.tasky.util.Constants.Companion.ACCESS_TOKEN
import com.example.tasky.util.Constants.Companion.ACCESS_TOKEN_EXPIRATION_STAMP
import com.example.tasky.util.Constants.Companion.FULL_NAME
import com.example.tasky.util.Constants.Companion.PREFERENCE_NAME
import com.example.tasky.util.Constants.Companion.REFRESH_TOKEN_KEY
import com.example.tasky.util.Constants.Companion.USER_ID
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = PREFERENCE_NAME)

class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {

        val refreshToken = stringPreferencesKey(REFRESH_TOKEN_KEY)
        val accessToken = stringPreferencesKey(ACCESS_TOKEN)
        val accessTokenExpirationStamp = longPreferencesKey(ACCESS_TOKEN_EXPIRATION_STAMP)
        val fullName = stringPreferencesKey(FULL_NAME)
        val userId = stringPreferencesKey(USER_ID)
    }

    /** Creating a datastore **/
    private val dataStore: DataStore<Preferences> = context.dataStore


    suspend fun saveUserDetails(
        accessToken: String,
        accessTokenExpirationTimestamp: Long,
        fullName: String,
        refreshToken: String,
        userId: String
    ) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.accessToken] = accessToken
            preferences[PreferenceKeys.accessTokenExpirationStamp] = accessTokenExpirationTimestamp
            preferences[PreferenceKeys.fullName] = fullName
            preferences[PreferenceKeys.refreshToken] = refreshToken
            preferences[PreferenceKeys.userId] = userId
        }
    }


    val readCurrentUserDetails: Flow<CurrentUser> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val getAccessToken = preferences[PreferenceKeys.accessToken] ?: ""
            val getAccessTokenExpirationTimeStamp = preferences[PreferenceKeys.accessTokenExpirationStamp] ?: 0
            val getFullName = preferences[PreferenceKeys.fullName] ?: ""
            val refreshToken = preferences[PreferenceKeys.refreshToken] ?: ""
            val userId = preferences[PreferenceKeys.userId] ?: ""
            CurrentUser(
                getAccessToken,
                getAccessTokenExpirationTimeStamp,
                getFullName,
                refreshToken,
                userId
            )
        }
}


data class CurrentUser(
    val accessToken: String,
    val accessTokenExpirationTimestamp: Long,
    val fullName: String,
    val refreshToken: String,
    val userId: String
)