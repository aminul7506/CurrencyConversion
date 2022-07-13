package paypay.currencycoversion.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import paypay.currencycoversion.R
import paypay.currencycoversion.models.EachCurrencyItem
import paypay.currencycoversion.viewHolders.CurrencyEachItemViewHolder

class CurrencyConversionListAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var dataList = mutableListOf<EachCurrencyItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CurrencyEachItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.each_grid_in_currency, parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CurrencyEachItemViewHolder).bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}