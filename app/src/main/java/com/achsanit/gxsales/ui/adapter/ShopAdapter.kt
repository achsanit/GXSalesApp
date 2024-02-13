package com.achsanit.gxsales.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.achsanit.gxsales.R
import com.achsanit.gxsales.data.local.entity.ShopItemEntity
import com.achsanit.gxsales.databinding.ItemShopBinding

class ShopAdapter : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemShopBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ShopItemEntity) {
            with(binding) {
                tvItemTitle.text = data.title
                tvPriceItem.text =
                    itemView.resources.getString(R.string.text_price_placeholder, data.price)
                tvStock.text = itemView.resources.getString(
                        R.string.text_stock_placeholder,
                        data.stock.toString()
                    )
                tvType.text =
                    itemView.resources.getString(R.string.text_type_placeholder, data.type)
                val taxPercent = "${data.taxPercentage}%"
                tvWTax.text = itemView.resources.getString(
                        R.string.text_tax_placeholder,
                        data.taxAmount,
                        taxPercent
                    )
                ivShopItem.setImageResource(data.image)
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<ShopItemEntity>() {
        override fun areItemsTheSame(oldItem: ShopItemEntity, newItem: ShopItemEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopItemEntity, newItem: ShopItemEntity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun setData(data: List<ShopItemEntity>) = differ.submitList(data)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }
}