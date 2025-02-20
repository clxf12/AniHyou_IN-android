package com.axiel7.anihyou.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object PreferencesDataStore {

    val ACCESS_TOKEN_PREFERENCE_KEY = stringPreferencesKey("access_token")
    val USER_ID_PREFERENCE_KEY = intPreferencesKey("user_id")
    val PROFILE_COLOR_PREFERENCE_KEY = stringPreferencesKey("profile_color")
    val SCORE_FORMAT_PREFERENCE_KEY = stringPreferencesKey("score_format")
    val THEME_PREFERENCE_KEY = stringPreferencesKey("theme")
    val LAST_TAB_PREFERENCE_KEY = intPreferencesKey("last_tab")
    val ANIME_LIST_SORT_PREFERENCE_KEY = stringPreferencesKey("anime_list_sort")
    val MANGA_LIST_SORT_PREFERENCE_KEY = stringPreferencesKey("manga_list_sort")
    val LIST_DISPLAY_MODE_PREFERENCE_KEY = stringPreferencesKey("list_display_mode")

    val Context.defaultPreferencesDataStore by preferencesDataStore(name = "default")

    /**
     * Gets the value by blocking the main thread
     */
    fun <T> DataStore<Preferences>.getValueSync(
        key: Preferences.Key<T>
    ) = runBlocking { data.first() }[key]

    @Composable
    fun <T> rememberPreference(
        key: Preferences.Key<T>,
        defaultValue: T?,
    ): MutableState<T?> {
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        val state = remember {
            context.defaultPreferencesDataStore.data
                .map {
                    it[key] ?: defaultValue
                }
        }.collectAsState(initial = defaultValue)

        return remember {
            object : MutableState<T?> {
                override var value: T?
                    get() = state.value
                    set(value) {
                        coroutineScope.launch {
                            context.defaultPreferencesDataStore.edit {
                                if (value != null) it[key] = value
                            }
                        }
                    }

                override fun component1() = value
                override fun component2(): (T?) -> Unit = { value = it }
            }
        }
    }
}