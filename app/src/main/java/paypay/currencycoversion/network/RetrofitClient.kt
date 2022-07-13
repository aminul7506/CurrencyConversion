package paypay.currencycoversion.helpers

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import paypay.currencycoversion.BuildConfig
import paypay.currencycoversion.network.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private var retrofit: Retrofit? = null
private var apiService: APIService? = null

private fun getRetrofitInstance(): Retrofit {
    if (retrofit == null) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().apply {
            addInterceptor(
                    Interceptor { chain ->
                        val builder = chain.request().newBuilder()
                        builder.header("X-App-Version", BuildConfig.VERSION_CODE.toString())
                        builder.header("X-App-Platform", "Android")
                        return@Interceptor chain.proceed(builder.build())
                    }
            )
        }
        if (BuildConfig.DEBUG){
            client.addInterceptor(interceptor);
        }
        retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_ROOT_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
    }
    return retrofit!!
}


fun getAPIService(): APIService {
    if (apiService == null) {
        apiService = getRetrofitInstance().create(APIService::class.java)
    }
    return apiService!!
}