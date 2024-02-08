package com.achsanit.gxsales.ui.features.lead.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.achsanit.gxsales.R
import com.achsanit.gxsales.databinding.FragmentFirstAddLeadBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class AddLeadFirstFragment : Fragment() {
    private var _binding: FragmentFirstAddLeadBinding? = null
    private val binding get() = _binding!!

    private val viewModel : AddLeadViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstAddLeadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            findNavController()
                .navigate(R.id.action_createUpdateLeadFragment_to_addLeadSecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}