package paypay.currencycoversion.models

data class CurrencyDbItemListWithBase (
        val base: String?,
        val dbCurrencyItemList: MutableList<CurrencyDbItem>
)