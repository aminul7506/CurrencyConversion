package paypay.currencycoversion.models
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class CurrencyApiModel (
        @SerializedName("disclaimer")
        val disclaimer: String?,

        @SerializedName("license")
        val license: String?,

        @SerializedName("timestamp")
        val timestamp: Long?,

        @SerializedName("base")
        val base: String?,

        @SerializedName("rates")
        val rates: Any?
)

data class EachCurrencyItem (
        val currencyName: String?,
        val currencyAmount: Double?
        )

