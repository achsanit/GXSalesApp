package com.achsanit.gxsales.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.achsanit.gxsales.R
import com.achsanit.gxsales.data.local.entity.LeadItemEntity
import com.achsanit.gxsales.databinding.ItemLeadBinding

class LeadsAdapter(private val onItemClick: (data: LeadItemEntity) -> Unit) : RecyclerView.Adapter<LeadsAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemLeadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LeadItemEntity) {
            val probabilityColor = when (data.probability) {
                "Pending" -> R.color.primary_yellow
                "Converted" -> R.color.primary_green
                else -> R.color.red
            }


            val statusColor = when (data.status) {
                "Consideration" -> R.color.primary_navy
                "Schedule" -> R.color.neutral_blue
                else -> R.color.primary_orange
            }

            with(binding) {
                root.setOnClickListener {
                    onItemClick(data)
                }

                tvFullName.text = data.fullName
                tvLeadNumber.text = data.numberLead
                tvAddress.text = data.address
                tvStatus.text = data.status
                cvStatus.setCardBackgroundColor(
                    ContextCompat.getColor(
                        this@ViewHolder.itemView.context,
                        statusColor
                    )
                )
                tvProbability.text = data.probability
                cvProbability.setCardBackgroundColor(
                    ContextCompat.getColor(
                        this@ViewHolder.itemView.context,
                        probabilityColor
                    )
                )
                tvAddDate.text = data.createdAt
                tvBranch.text = data.branchOffice
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<LeadItemEntity>() {
        override fun areItemsTheSame(oldItem: LeadItemEntity, newItem: LeadItemEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LeadItemEntity, newItem: LeadItemEntity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun setData(data: List<LeadItemEntity>) = differ.submitList(data)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemLeadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }
}