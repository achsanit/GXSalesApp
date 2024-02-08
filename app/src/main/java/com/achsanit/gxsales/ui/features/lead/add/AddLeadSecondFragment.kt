package com.achsanit.gxsales.ui.features.lead.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.achsanit.gxsales.databinding.FragmentSecondAddLeadBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class AddLeadSecondFragment : Fragment() {

    private var _binding: FragmentSecondAddLeadBinding? = null
    private val binding get() = _binding!!
    private val viewModel : AddLeadViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSecondAddLeadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}