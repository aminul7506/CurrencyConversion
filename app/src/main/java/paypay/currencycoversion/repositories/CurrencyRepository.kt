package paypay.currencycoversion.repositories

import android.content.Context
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import paypay.currencycoversion.databases.AppDatabase
import paypay.currencycoversion.helpers.APIService
import paypay.currencycoversion.helpers.Constants
import paypay.currencycoversion.helpers.Utils
import paypay.currencycoversion.helpers.getAPIService
import paypay.currencycoversion.models.ApiGenericError
import paypay.currencycoversion.models.CurrencyApiModel
import paypay.currencycoversion.models.CurrencyDbItemListWithBase
import paypay.currencycoversion.preferences.AppPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.Executors

class CurrencyRepository constructor(private val context: Context) {

    private val mutableLiveData: MutableLiveData<Any> = MutableLiveData()
    private val databaseOperationExecutors = Executors.newSingleThreadExecutor()

    private var hasPrevData = false;

    init {
        getHomeContentDataFromDbAndApiIfNeeded()
    }

    private fun insertAndPostHomeContentData(currencyApiModel: CurrencyApiModel) {
        val currentTime = System.currentTimeMillis()
        val listOfDbObjects = Utils.convertCurrencyApiModelToListOfCurrencyDbItem(currencyApiModel, currentTime)
        mutableLiveData.postValue(CurrencyDbItemListWithBase(currencyApiModel.base, listOfDbObjects))
        Timber.d("Inserting currency data into database after fetch success")
        databaseOperationExecutors.execute {
            AppPreference.getPreference().setBaseCurrency(currencyApiModel.base ?: "", context)
            AppPreference.getPreference().setLastCurrencyApiFetchTime(currentTime, context)
            AppDatabase.getDatabase(context.applicationContext).dao().insertListOfCurrencyDbItem(listOfDbObjects)
        }
    }

    fun getHomeContentData() : LiveData<Any> {
        return mutableLiveData
    }

    private fun getHomeContentDataFromDbAndApiIfNeeded() {
        databaseOperationExecutors.execute {
            val base = AppPreference.getPreference().getBaseCurrency(context)
            val list = AppDatabase.getDatabase(context.applicationContext).dao().getCurrencyDbListItem(base)
            val currencyDbItemListWithBase = CurrencyDbItemListWithBase(base, list)
            Timber.d("Getting currency data from database")
            if (list.size > 0) {
                hasPrevData  = true
                mutableLiveData.postValue(currencyDbItemListWithBase)
            } else {
                fetchCurrencyData()
            }
        }
        val lastApiFetchTime = AppPreference.getPreference().getLastCurrencyAPiFetchTime(context)
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastApiFetchTime >= Constants.API_FETCH_INTERVAL) {
            fetchCurrencyData()
        }
    }

    fun fetchCurrencyData() {
        val apiService: APIService = getAPIService()
        val call: Call<CurrencyApiModel> = apiService.fetchCurrencyData(Constants.CURRENCY_API_APP_ID)
        call.enqueue(object : Callback<CurrencyApiModel?> {
            override fun onResponse(call: Call<CurrencyApiModel?>, response: Response<CurrencyApiModel?>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        insertAndPostHomeContentData(response.body()!!)
                    } else {
                        if (!hasPrevData) {
                            val apiGenericError = ApiGenericError(true, 404, "nil", "message body nil")
                            mutableLiveData.postValue(apiGenericError)
                        }
                    }
                } else {
                    if (!hasPrevData) {
                        if (response.errorBody() != null) {
                            val parser = JsonParser()
                            val jsonTree: JsonElement
                            try {
                                jsonTree = parser.parse(response.errorBody()!!.string())
                                val jsonObject = jsonTree.asJsonObject
                                val apiGenericError = Gson().fromJson(jsonObject.toString(), ApiGenericError::class.java)
                                mutableLiveData.postValue(apiGenericError)
                            } catch (e: Exception) {
                                val apiGenericError = ApiGenericError(true, 404, "unknown", "unknown")
                                mutableLiveData.postValue(apiGenericError)
                            }
                        } else {
                            val apiGenericError = ApiGenericError(true, 404, "unknown", "unknown")
                            mutableLiveData.postValue(apiGenericError)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CurrencyApiModel?>, t: Throwable) {
                if (!hasPrevData) {
                    mutableLiveData.postValue(null)
                }
            }
        })
    }
}