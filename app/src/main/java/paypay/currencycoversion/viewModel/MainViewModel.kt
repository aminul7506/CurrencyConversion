package paypay.currencycoversion.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import paypay.currencycoversion.repositories.CurrencyRepository
import paypay.currencycoversion.models.CurrencyDbItemListWithBase

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var currencyRepository: CurrencyRepository

    init {
        currencyRepository = CurrencyRepository(application)
    }

    fun getHomeContentData(): LiveData<Any> {
        return currencyRepository.getHomeContentData()
    }

    fun fetchCurrencyData() {
        return currencyRepository.fetchCurrencyData()
    }
}