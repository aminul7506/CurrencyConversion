package paypay.currencycoversion.helpers

import paypay.currencycoversion.models.CurrencyApiModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface APIService {
    @GET("latest.json")
    fun fetchCurrencyData(@Query("app_id") addId: String): Call<CurrencyApiModel>
}