package com.amirhparhizgar.revision.service.setting


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.amirhparhizgar.revision.model.ThemeMode
import javax.inject.Inject

class SettingStore @Inject constructor(val dataStore: DataStore<Preferences>) {
    val todoReminding = TodoReminding()

    inner class TodoReminding : SimpleSetting<Boolean>(dataStore) {
        override val key: Preferences.Key<Boolean>
            get() = booleanPreferencesKey("todo reminding enable")
        public override val default: Boolean
            get() = false
    }

    val remindingHour = RemindingHour()

    inner class RemindingHour : SimpleSetting<Int>(dataStore) {
        override val key: Preferences.Key<Int>
            get() = intPreferencesKey("reminding hour")
        public override val default: Int
            get() = 8
    }

    val themeSetting = ThemeSetting()

    inner class ThemeSetting : SimpleSetting<Int>(dataStore) {
        override val key: Preferences.Key<Int>
            get() = intPreferencesKey("theme")
        override val default: Int
            get() = ThemeMode.AUTOMATIC.id
    }
}


