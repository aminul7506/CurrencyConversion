package paypay.currencycoversion.databases

import android.content.Context
import androidx.room.*
import paypay.currencycoversion.models.CurrencyDbItem


@androidx.room.Database(entities = [
    CurrencyDbItem::class
], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): AppDatabaseDao

    companion object {
        private var instance: AppDatabase? = null
        private const val NAME = "app_db"

        fun getDatabase(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(context.applicationContext,
                                AppDatabase::class.java, NAME)
                                .fallbackToDestructiveMigration()
                                .build()
                    }
                }
            }
            return instance!!
        }
    }
}