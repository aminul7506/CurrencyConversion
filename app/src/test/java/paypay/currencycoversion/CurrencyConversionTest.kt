package paypay.currencycoversion

import org.junit.Assert
import org.junit.Test
import paypay.currencycoversion.helpers.Utils
import paypay.currencycoversion.models.EachCurrencyItem

class CurrencyConversionTest {
    @Test
    fun conversion_isCorrect() {
        val map = HashMap<String, Double>()
        map["USD"] = 1.0
        map["AED"] = 3.6731
        map["AFN"] = 87.589593
        map["ALL"] = 115.945018
        val prevCurrencyList: MutableList<EachCurrencyItem> = mutableListOf()
        prevCurrencyList.add(EachCurrencyItem("USD", 1.0))
        prevCurrencyList.add(EachCurrencyItem("AED", 3.6731))
        prevCurrencyList.add(EachCurrencyItem("AFN", 87.589593))
        prevCurrencyList.add(EachCurrencyItem("ALL", 115.945018))
        val newList = Utils.convertNewSelectedItemToCurrencyList("AED",
                "USD", map, prevCurrencyList, 5.0)

        for (item in newList) {
            if (item.currencyName == "AFN") {
                Assert.assertEquals(119.231157, item.currencyAmount!!, 0.1)
            }
        }
    }
}