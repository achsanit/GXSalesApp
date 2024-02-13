package com.achsanit.gxsales.ui.features.shop

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
import com.achsanit.gxsales.data.local.entity.ShopItemEntity
import com.achsanit.gxsales.databinding.FragmentShopBinding
import com.achsanit.gxsales.ui.adapter.ShopAdapter
import com.achsanit.gxsales.utils.Resource
import com.achsanit.gxsales.utils.makeGone
import com.achsanit.gxsales.utils.makeVisible
import com.achsanit.gxsales.utils.onNetworkEvent
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShopViewModel by viewModel()
    private val shopAdapter: ShopAdapter by lazy { ShopAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpStateListener()
        with(binding) {
            rvShop.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = shopAdapter
            }
            ibBackToolbar.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setUpStateListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.shopItemState.collect(::shopItemCollector)
            }
        }
    }

    private fun shopItemCollector(data: Resource<List<ShopItemEntity>>) {
        with(binding) {
            data.collect(
                onLoading = {
                    pbShop.makeVisible()
                },
                onSuccess = {
                    pbShop.makeGone()
                    data.data?.let { listItem -> shopAdapter.setData(listItem) }
                },
                onError = { s: String?, i: Int ->
                    pbShop.makeGone()
                    onNetworkEvent(i, s)
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}