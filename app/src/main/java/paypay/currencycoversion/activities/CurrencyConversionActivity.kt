package paypay.currencycoversion.activities

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import paypay.currencycoversion.R
import paypay.currencycoversion.adapters.OuterRvAdapterInConversationActivity
import paypay.currencycoversion.helpers.Utils
import paypay.currencycoversion.interfaces.CurrencyInputChangeListener
import paypay.currencycoversion.models.ApiGenericError
import paypay.currencycoversion.models.CurrencyDbItemListWithBase
import paypay.currencycoversion.models.EachCurrencyItem
import paypay.currencycoversion.viewHolders.CurrencyInfoViewHolder
import paypay.currencycoversion.viewModel.MainViewModel

class CurrencyConversionActivity: AppCompatActivity() {

    private lateinit var rvMain: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvSomethingWrong: AppCompatTextView
    private lateinit var tvRetry: AppCompatTextView

    private lateinit var mainViewModel: MainViewModel

    private lateinit var outerRvAdapterInConversionActivity: OuterRvAdapterInConversationActivity

    private var listOfCurrencyAndAmount = mutableListOf<EachCurrencyItem>()
    private var mapOfCurrencyAndAmount = HashMap<String, Double>()
    private var currentSelectedCurrency = ""
    private var currentSelectedAmount = 1.0
    private var baseCurrency = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.currency_conversion_activity)
        initGUI()
    }

    private fun initGUI() {
        progressBar = findViewById(R.id.progressBar)
        initViewModel()
        initRecyclerView()
        initRetryLayout()
        getHomeContentData()
    }

    private fun initRecyclerView() {
        rvMain = findViewById(R.id.rvMain)
        rvMain.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        outerRvAdapterInConversionActivity = OuterRvAdapterInConversationActivity(object : CurrencyInputChangeListener {
            override fun onCurrencyChange(newCurrency: String) {
                doTAskOnCurrencyChange(newCurrency, currentSelectedAmount)
            }

            override fun onAmountChange(newAmount: Double) {
                doTAskOnCurrencyChange(currentSelectedCurrency, newAmount)
            }
        })
        rvMain.adapter = outerRvAdapterInConversionActivity
        rvMain.visibility = View.GONE
    }

    private fun initRetryLayout() {
        tvSomethingWrong = findViewById(R.id.tvSomethingWrong)
        tvRetry = findViewById(R.id.tvRetry)
        tvRetry.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            rvMain.visibility = View.GONE
            tvSomethingWrong.visibility = View.GONE
            tvRetry.visibility = View.GONE
            mainViewModel.fetchCurrencyData()
        }
    }

    private fun layoutsOnDataFetchSuccess() {
        rvMain.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        tvSomethingWrong.visibility = View.GONE
        tvRetry.visibility = View.GONE
    }

    private fun layoutsOnDataFetchFailed(networkError: Boolean) {
        rvMain.visibility = View.GONE
        progressBar.visibility = View.GONE
        if (networkError) {
            tvSomethingWrong.text = getString(R.string.something_wrong_no_internet)
        } else {
            tvSomethingWrong.text = getString(R.string.something_wrong)
        }
        tvSomethingWrong.visibility = View.VISIBLE
        tvRetry.visibility = View.VISIBLE
    }

    private fun initViewModel() {
        this.mainViewModel = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)).
                get(MainViewModel::class.java)
    }

    private fun getHomeContentData() {
        mainViewModel.getHomeContentData().observe(this) { newData ->
            if (newData == null) {
                layoutsOnDataFetchFailed(true)
                return@observe
            }
            if (newData is ApiGenericError) {
                layoutsOnDataFetchFailed(false)
                return@observe
            }
            if (newData is CurrencyDbItemListWithBase) {
                layoutsOnDataFetchSuccess()
                currentSelectedCurrency = newData.base ?: ""
                listOfCurrencyAndAmount = Utils.convertDBCurrencyModelToEachCurrencyItemForUnitAmount(newData.dbCurrencyItemList,
                        currentSelectedAmount)
                mapOfCurrencyAndAmount = Utils.convertEachCurrencyItemIntoMap(listOfCurrencyAndAmount)
                baseCurrency = newData.base ?: ""
                outerRvAdapterInConversionActivity.setData(listOfCurrencyAndAmount,
                        newData.base ?: "")
            }
        }
    }

    private fun doTAskOnCurrencyChange(newCurrency: String, newAmount: Double) {
        listOfCurrencyAndAmount = Utils.convertNewSelectedItemToCurrencyList(newCurrency, baseCurrency,
                mapOfCurrencyAndAmount, listOfCurrencyAndAmount, newAmount)
        currentSelectedCurrency = newCurrency
        currentSelectedAmount = newAmount
        outerRvAdapterInConversionActivity.setData(listOfCurrencyAndAmount, currentSelectedCurrency)
    }

    override fun onDestroy() {
        super.onDestroy()
        val viewHolder: RecyclerView.ViewHolder? = rvMain.findViewHolderForAdapterPosition(0)
        viewHolder.let {
            if (it is CurrencyInfoViewHolder) {
                it.freeHandler()
            }
        }
    }
}