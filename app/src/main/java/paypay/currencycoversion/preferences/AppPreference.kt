package paypay.currencycoversion.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE

class AppPreference {

    companion object {
        private var instance: AppPreference? = null

        private var NAME = "app_pref"

        private const val BASE_CURRENCY_PREF_KEY = "base_currency";
        private const val LAST_CURRENCY_API_FETCH_TIME = "last_currency_api_fetch_time";

        fun getPreference(): AppPreference {
            if (instance == null) {
                synchronized(AppPreference::class.java) {
                    if (instance == null) {
                        instance = AppPreference()
                    }
                }
            }
            return instance!!
        }
    }

    fun setBaseCurrency(value: String, context: Context) {
        context.applicationContext.getSharedPreferences(NAME, MODE_PRIVATE).edit().putString(BASE_CURRENCY_PREF_KEY, value).apply()
    }

    fun getBaseCurrency(context: Context) : String {
        return context.applicationContext.getSharedPreferences(NAME, MODE_PRIVATE).getString(BASE_CURRENCY_PREF_KEY, "") ?: ""
    }

    fun setLastCurrencyApiFetchTime(value: Long, context: Context){
        context.applicationContext.getSharedPreferences(NAME, MODE_PRIVATE).edit().putLong(LAST_CURRENCY_API_FETCH_TIME, value).apply()
    }

    fun getLastCurrencyAPiFetchTime(context: Context) : Long {
        return context.applicationContext.getSharedPreferences(NAME, MODE_PRIVATE).getLong(LAST_CURRENCY_API_FETCH_TIME, -1)
    }
}