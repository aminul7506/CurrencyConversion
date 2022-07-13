package paypay.currencycoversion.helpers

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import paypay.currencycoversion.models.CurrencyApiModel
import paypay.currencycoversion.models.CurrencyDbItem
import paypay.currencycoversion.models.EachCurrencyItem
import timber.log.Timber

object Utils {

  fun convertDBCurrencyModelToEachCurrencyItemForUnitAmount(listOfDbItem: MutableList<CurrencyDbItem>,
                                                            baseCurrencyAmount: Double) : MutableList<EachCurrencyItem> {
      val list = mutableListOf<EachCurrencyItem>()
      for (item in listOfDbItem) {
          val totalCurrency = baseCurrencyAmount * item.amount
          list.add(EachCurrencyItem(item.currency, totalCurrency))
        }
      return list
    }

    fun convertEachCurrencyItemIntoMap(list: MutableList<EachCurrencyItem>) :
            HashMap<String, Double> {
        val map = HashMap<String, Double>()
        for (item in list) {
            if (item.currencyName != null && item.currencyAmount != null) {
                map[item.currencyName] = item.currencyAmount
            }
        }
        return map
    }

    fun convertNewSelectedItemToCurrencyList(currentCurrencyName: String,
                                             baseCurrency: String,
                                             map: HashMap<String, Double>,
                                             prevCurrencyList: MutableList<EachCurrencyItem>,
                                             currentAmount: Double): MutableList<EachCurrencyItem> {
        val baseCurrencyAmount: Double? = map[baseCurrency]
        val currentCurrencyAmount: Double? = map[currentCurrencyName]
        var perCurrency = 0.0
        if (baseCurrencyAmount != null && currentCurrencyAmount != null) {
            if (baseCurrencyAmount != 0.0) {
                perCurrency = baseCurrencyAmount / currentCurrencyAmount
            }
        }
        val newList: MutableList<EachCurrencyItem> = mutableListOf()
        for (item in prevCurrencyList) {
            val totalCurrency = perCurrency * (map[item.currencyName] ?: 0.0) * currentAmount
            newList.add(EachCurrencyItem(item.currencyName, totalCurrency))
        }
        return newList
    }

    fun convertCurrencyApiModelToListOfCurrencyDbItem(apiModel: CurrencyApiModel, currentTime: Long):
            MutableList<CurrencyDbItem> {
        val list = mutableListOf<CurrencyDbItem>()
        apiModel.rates?.let {
            try {
                val jsonObject = JSONObject(it.toString())
                val keys = jsonObject.keys()
                while (keys.hasNext()) {
                    val currency = keys.next()
                    val currencyDbItem = CurrencyDbItem()
                    currencyDbItem.id = 0
                    currencyDbItem.currency = currency.uppercase()
                    currencyDbItem.amount = jsonObject.optDouble(currency)
                    currencyDbItem.base = apiModel.base ?: ""
                    currencyDbItem.time = currentTime
                    list.add(currencyDbItem)
                }
            } catch (jsonException: JSONException) {
                jsonException.printStackTrace()
            }
        }
        return list
    }

}