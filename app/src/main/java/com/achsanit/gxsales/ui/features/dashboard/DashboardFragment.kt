package com.achsanit.gxsales.ui.features.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.achsanit.gxsales.R
import com.achsanit.gxsales.data.local.entity.LeadDashboardEntity
import com.achsanit.gxsales.data.local.entity.ProfileEntity
import com.achsanit.gxsales.databinding.FragmentDashboardBinding
import com.achsanit.gxsales.ui.adapter.LeadStatusAdapter
import com.achsanit.gxsales.utils.DateHelper
import com.achsanit.gxsales.utils.Resource
import com.achsanit.gxsales.utils.makeGone
import com.achsanit.gxsales.utils.makeVisible
import com.achsanit.gxsales.utils.onNetworkEvent
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<DashboardViewModel>()
    private val leadAdapter: LeadStatusAdapter by lazy { LeadStatusAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpStateListener()

        with(binding) {
            tvRangeDate.text = resources.getString(
                R.string.text_current_date_range_in_month,
                DateHelper.getFirstDayInMonth(),
                DateHelper.getLastDayInMonth()
            )

            rvLeadsItem.apply {
                adapter = leadAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }
    }

    private fun setUpStateListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
                viewModel.leadsDashboardState.collect(::leadsStateCollector)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
                viewModel.profileState.collect(::profileStateCollector)
            }
        }
    }

    private fun profileStateCollector(data: Resource<ProfileEntity>) {
        with(binding) {
            when(data) {
                is Resource.Loading -> {
                    pbProfileState.makeVisible()
                }
                is Resource.Success -> {
                    pbProfileState.makeGone()
                    tvUserName.text = data.data?.name.toString()
                    tvUserEmail.text = data.data?.email.toString()
                }
                else -> {
                    pbProfileState.makeGone()
                    onNetworkEvent(data.codeError)
                }
            }
        }
    }

    private fun leadsStateCollector(data: Resource<List<LeadDashboardEntity>>) {
        with(binding) {
            when (data) {
                is Resource.Loading -> {
                    pbLeadsState.makeVisible()
                }
                is Resource.Success -> {
                    pbLeadsState.makeGone()
                    leadAdapter.setData(data.data ?: emptyList())
                }
                else -> {
                    pbLeadsState.makeGone()
                    onNetworkEvent(data.codeError)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getLeads()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}