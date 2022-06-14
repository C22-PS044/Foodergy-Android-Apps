package com.bangkitacademy.foodergyapps.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.bangkitacademy.foodergyapps.data.model.UserSession
import com.bangkitacademy.foodergyapps.data.response.LoginItem
import dagger.hilt.android.qualifiers.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import javax.inject.*

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreference @Inject constructor(@ApplicationContext val context: Context) {
    private val dataStore =context.dataStore

    fun getUser(): Flow<UserSession>{
        return dataStore.data.map{ preferences ->
            UserSession(
//                preferences[NAME_KEY] ?:"",
                preferences[EMAIL_KEY]?: "",
//                preferences[ALERGI_KEY]?: "",
//                preferences[LOGIN_STATE] ?: false
            )

        }
    }
//    fun getUser2(): Flow<LoginItem>{
//        return dataStore.data.map{ preferences ->
//            LoginItem(
//                preferences[NAME_KEY] ?:"",
//                preferences[EMAIL_KEY]?: "",
//                preferences[PASSWORD_KEY]?: "",
//                preferences[ALERGI_KEY]?:""
//            )
//        }
//    }

    suspend fun setUser(user: UserSession) {
        dataStore.edit { preferences ->
//            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
//            preferences[ALERGI_KEY] = user.alergi
//            preferences[LOGIN_STATE] = user.isLogin
        }
    }

    suspend fun setUser2(user: LoginItem) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[EMAIL_KEY] = user.email
            preferences[PASSWORD_KEY] = user.password
            preferences[ALERGI_KEY] = user.alergi
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
//            preferences[LOGIN_STATE] = false
//            preferences[NAME_KEY] = ""
            preferences[EMAIL_KEY] = ""
//            preferences[PASSWORD_KEY] = ""
//            preferences[ALERGI_KEY] = ""
        }
    }
    suspend fun logout2() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val ALERGI_KEY = stringPreferencesKey("alergi")
        private val LOGIN_STATE = booleanPreferencesKey("state")

    }
}