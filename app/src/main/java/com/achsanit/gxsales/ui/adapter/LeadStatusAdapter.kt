package com.achsanit.gxsales.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.achsanit.gxsales.data.local.entity.LeadDashboardEntity
import com.achsanit.gxsales.databinding.ItemLeadDashboardBinding

class LeadStatusAdapter: RecyclerView.Adapter<LeadStatusAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val binding: ItemLeadDashboardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LeadDashboardEntity) {
            with(binding) {
                tvTitleLead.text = data.name
                tvCountLead.text = data.total.toString()
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<LeadDashboardEntity>() {
        override fun areItemsTheSame(oldItem: LeadDashboardEntity, newItem: LeadDashboardEntity): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: LeadDashboardEntity, newItem: LeadDashboardEntity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun setData(data: List<LeadDashboardEntity>) = differ.submitList(data)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemLeadDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }
}