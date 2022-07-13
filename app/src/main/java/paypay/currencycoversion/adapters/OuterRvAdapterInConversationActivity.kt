package paypay.currencycoversion.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import paypay.currencycoversion.interfaces.CurrencyInputChangeListener
import paypay.currencycoversion.R
import paypay.currencycoversion.models.EachCurrencyItem
import paypay.currencycoversion.viewHolders.CurrencyInfoViewHolder
import paypay.currencycoversion.viewHolders.CurrencyListItemViewHolder

class OuterRvAdapterInConversationActivity(private val currencyInputChangeListener: CurrencyInputChangeListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_CURRENCY_INFO = 0
        const val ITEM_CURRENCY_LIST = 1
    }

    private var dataList: MutableList<EachCurrencyItem> = mutableListOf()
    private var currentSelectedCurrency = ""

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        if (position == ITEM_CURRENCY_INFO) {
            return CurrencyInfoViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_currency_info, parent, false
                    )
            )
        }
        return CurrencyListItemViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_currency_list, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is CurrencyInfoViewHolder) {
            viewHolder.bind(dataList, currentSelectedCurrency, currencyInputChangeListener)
        } else if (viewHolder is CurrencyListItemViewHolder) {
            viewHolder.bind(dataList)
        }
    }

    fun setData(dataList: MutableList<EachCurrencyItem>, currentSelectedCurrency: String) {
        this.currentSelectedCurrency = currentSelectedCurrency
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return ITEM_CURRENCY_INFO
        }
        return ITEM_CURRENCY_LIST
    }
}
