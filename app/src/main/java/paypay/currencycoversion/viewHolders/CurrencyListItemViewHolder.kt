package paypay.currencycoversion.viewHolders

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import paypay.currencycoversion.R
import paypay.currencycoversion.adapters.CurrencyConversionListAdapter
import paypay.currencycoversion.decorations.GridSpacingItemDecoration
import paypay.currencycoversion.models.EachCurrencyItem

class CurrencyListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val rvCurrencyList = itemView.findViewById<RecyclerView>(R.id.rvCurrencyList)

    init {
        val appLeftRightPadding: Int =
                itemView.context.resources.getDimension(R.dimen.app_left_right_padding).toInt()
        val numberOfColumns: Int = itemView.context.resources.
            getInteger(R.integer.number_of_items_per_grid_currency_list)
        val itemDecoration = GridSpacingItemDecoration(numberOfColumns, appLeftRightPadding, true)
        rvCurrencyList.addItemDecoration(itemDecoration)
        rvCurrencyList.layoutManager = GridLayoutManager(itemView.context, numberOfColumns)
        rvCurrencyList.adapter = CurrencyConversionListAdapter()
    }

    fun bind(dataList: MutableList<EachCurrencyItem>) {
        setData(dataList)
    }

    private fun setData(dataList: MutableList<EachCurrencyItem>) {
        (rvCurrencyList.adapter as CurrencyConversionListAdapter).dataList = dataList
        rvCurrencyList.adapter?.notifyDataSetChanged()
    }

}