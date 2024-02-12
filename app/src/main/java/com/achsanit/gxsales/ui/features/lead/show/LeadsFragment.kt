package com.achsanit.gxsales.ui.features.lead.show

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.achsanit.gxsales.data.local.entity.LeadItemEntity
import com.achsanit.gxsales.databinding.FragmentLeadsBinding
import com.achsanit.gxsales.ui.adapter.LeadsAdapter
import com.achsanit.gxsales.utils.Resource
import com.achsanit.gxsales.utils.makeGone
import com.achsanit.gxsales.utils.makeVisible
import com.achsanit.gxsales.utils.onNetworkEvent
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LeadsFragment : Fragment() {

    private var _binding: FragmentLeadsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<LeadsViewModel>()
    private val leadsAdapter by lazy {
        LeadsAdapter{
            //TODO: nav controller to update fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeadsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()

        with(binding) {
            rvLeads.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = leadsAdapter
            }
            ibBackToolbar.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                viewModel.leadsState.collect(::leadsStateCollector)
            }
        }
    }

    private fun leadsStateCollector(data: Resource<List<LeadItemEntity>>) {
        with(binding) {
            when(data) {
                is Resource.Loading -> {
                    pbLeadsState.makeVisible()
                }
                is Resource.Success -> {
                    pbLeadsState.makeGone()
                    data.data?.let { leadsAdapter.setData(it) }
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