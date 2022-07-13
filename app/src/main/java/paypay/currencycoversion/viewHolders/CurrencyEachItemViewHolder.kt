package paypay.currencycoversion.viewHolders

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import paypay.currencycoversion.R
import paypay.currencycoversion.models.EachCurrencyItem

class CurrencyEachItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvCurrencyCode = itemView.findViewById<AppCompatTextView>(R.id.tvCurrencyCode)
    private val tvCurrencyAmount = itemView.findViewById<AppCompatTextView>(R.id.tvCurrencyAmount)

    fun bind(currencyItem: EachCurrencyItem) {
        tvCurrencyCode.text = currencyItem.currencyName ?: ""

        "%.2f".format(currencyItem.currencyAmount ?: 0.0f).toDouble().toString().also {
            tvCurrencyAmount.text = it
        }
    }
}