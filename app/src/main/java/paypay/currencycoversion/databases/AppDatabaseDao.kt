package paypay.currencycoversion.databases

import androidx.room.*
import paypay.currencycoversion.models.CurrencyDbItem

@Dao
abstract class AppDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListOfCurrencyDbItem(list: List<CurrencyDbItem>)

    @Query("SELECT * FROM currency_table WHERE base = :base ORDER BY currency ASC")
    abstract fun getCurrencyDbListItem(base: String) : MutableList<CurrencyDbItem>
}