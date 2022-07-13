package paypay.currencycoversion.models

import com.google.gson.annotations.SerializedName

data class ApiGenericError (
        @SerializedName("error")
        val disclaimer: Boolean?,

        @SerializedName("status")
        val status: Int?,

        @SerializedName("message")
        val message: String?,

        @SerializedName("description")
        val description: String?
)