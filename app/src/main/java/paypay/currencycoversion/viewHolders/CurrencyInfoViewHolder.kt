package paypay.currencycoversion.viewHolders

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.RecyclerView
import paypay.currencycoversion.interfaces.CurrencyInputChangeListener
import paypay.currencycoversion.R
import paypay.currencycoversion.models.EachCurrencyItem


class CurrencyInfoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), AdapterView.OnItemSelectedListener {

    val etAmount = itemView.findViewById<AppCompatEditText>(R.id.etAmount)

    private var spinnerForCurrencySelection = itemView.findViewById<Spinner>(R.id.spinnerForCurrencySelection)
    private var currentSelectedCurrency: String = ""

    private var currencyInputChangeListener: CurrencyInputChangeListener? = null

    private var runnableForAmountChange: Runnable? = null
    private var handlerForAmountChange: Handler? = null

    init {
        val dropDownSpinAdapter: ArrayAdapter<String> = ArrayAdapter<String>(itemView.context,
                R.layout.currency_selected_item, mutableListOf())
        dropDownSpinAdapter.setDropDownViewResource(R.layout.currency_drop_down_item)
        spinnerForCurrencySelection.adapter = dropDownSpinAdapter
        spinnerForCurrencySelection.onItemSelectedListener = this
    }

    fun bind(
            dataList: MutableList<EachCurrencyItem>, currentSelectedCurrency: String,
            listener: CurrencyInputChangeListener,
    ) {
        this.currencyInputChangeListener = listener
        setData(dataList, currentSelectedCurrency)
        onTextChange(listener)
    }

    private fun setData(dataList: MutableList<EachCurrencyItem>, currentSelectedCurrency: String) {
        val dropDownItemList = mutableListOf<String>()
        for (item in dataList) {
            item.currencyName?.let {
                dropDownItemList.add(it)
            }
        }
        val adapter = spinnerForCurrencySelection.adapter as ArrayAdapter<String>
        adapter.addAll(dropDownItemList)
        adapter.notifyDataSetChanged()
        spinnerForCurrencySelection.setSelection(adapter.getPosition(currentSelectedCurrency))
        this.currentSelectedCurrency = currentSelectedCurrency
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        currentSelectedCurrency = spinnerForCurrencySelection.selectedItem.toString()

        currencyInputChangeListener?.onCurrencyChange(currentSelectedCurrency)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun taskOnTextChange(listener: CurrencyInputChangeListener, amount: Double) {
        if (handlerForAmountChange == null) {
            handlerForAmountChange = Handler(Looper.getMainLooper())
        }
        runnableForAmountChange?.let {
            handlerForAmountChange?.removeCallbacks(it)
        }
        runnableForAmountChange = Runnable {
            listener.onAmountChange(amount)
        }
        runnableForAmountChange?.let {
            handlerForAmountChange?.postDelayed(it, 300)
        }
    }

    fun freeHandler() {
        runnableForAmountChange?.let {
            handlerForAmountChange?.removeCallbacks(it)
        }
    }

    private fun onTextChange(listener: CurrencyInputChangeListener) {
        etAmount.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(seq: CharSequence?, start: Int, before: Int, count: Int) {
                seq?.let { s->
                    if (s.isEmpty()) {
                        etAmount.error = null
                    } else {
                        try {
                            val amount = s.toString().toDouble()
                            if (amount >= 0) {
                                etAmount.error = null
                                taskOnTextChange(listener, amount)
                            } else {
                                etAmount.error = etAmount.context.resources.getString(R.string.enter_a_valid_number)
                            }
                        } catch (exception: Exception) {
                            exception.printStackTrace()
                            etAmount.error = etAmount.context.resources.getString(R.string.enter_a_valid_number)
                        }
                    }
                }
            }
        })
    }
}