package com.achsanit.gxsales.ui.features

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.achsanit.gxsales.R
import com.achsanit.gxsales.databinding.FragmentDashboardBinding
import com.achsanit.gxsales.ui.adapter.LeadStatusAdapter
import com.achsanit.gxsales.utils.DateHelper
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<DashboardViewModel>()
    private val leadAdapter: LeadStatusAdapter by lazy { LeadStatusAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvRangeDate.text = resources.getString(
                R.string.text_current_date_range_in_month,
                DateHelper.getFirstDayInMonth(),
                DateHelper.getLastDayInMonth()
            )

            leadAdapter.setData(viewModel.listLeadStatus)
            rvLeadsItem.apply {
                adapter = leadAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }

    }

}