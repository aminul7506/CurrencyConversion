package paypay.currencycoversion.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "currency_table",
        indices = [Index(value = ["currency", "base"], unique = true)])

class CurrencyDbItem() {

    @field:ColumnInfo(name = "id", defaultValue = "0")
    @PrimaryKey(autoGenerate = true)
    var id :Int = 0

    @field:ColumnInfo(name = "currency")
    var currency : String = ""

    @field: ColumnInfo(name = "amount")
    var amount : Double = 0.0

    @field:ColumnInfo(name = "time")
    var time : Long = 0

    @field:ColumnInfo(name = "base")
    var base : String = ""
}
