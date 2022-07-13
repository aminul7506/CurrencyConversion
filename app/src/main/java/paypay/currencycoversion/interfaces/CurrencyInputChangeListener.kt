package paypay.currencycoversion.interfaces

interface CurrencyInputChangeListener {
    fun onCurrencyChange(newCurrency: String)
    fun onAmountChange(newAmount: Double)
}