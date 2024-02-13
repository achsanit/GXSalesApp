package com.achsanit.gxsales.ui.features.lead.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.achsanit.gxsales.R
import com.achsanit.gxsales.ui.features.lead.add.AddLeadViewModel.UIEvent.*
import com.achsanit.gxsales.databinding.FragmentSecondAddLeadBinding
import com.achsanit.gxsales.ui.dialog.SuccessDialogFragment
import com.achsanit.gxsales.utils.Resource
import com.achsanit.gxsales.utils.makeGone
import com.achsanit.gxsales.utils.makeVisible
import com.achsanit.gxsales.utils.onNetworkEvent
import com.achsanit.gxsales.utils.setEnable
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class AddLeadSecondFragment : Fragment() {

    private var _binding: FragmentSecondAddLeadBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddLeadViewModel by activityViewModel()
    private val navArgs: AddLeadSecondFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSecondAddLeadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSettingsData()
        setUpDataListener()
        setupTextChangeListener()

        with(binding) {
            edtNotes.setText(viewModel.mainData.value.notes)

            ibBackToolbar.setOnClickListener {
                findNavController().popBackStack()
            }
            btnPrevious.setOnClickListener {
                findNavController().popBackStack()
            }
            btnSubmit.setOnClickListener {
                viewModel.dispatch(OnCreateLead(navArgs.idLead))
            }
        }
    }

    private fun setUpDataListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.leadTypeState.collect(::leadTypeCollector)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.leadChannelState.collect(::leadChannelCollector)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.leadMediaState.collect(::leadMediaCollector)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.leadSourceState.collect(::leadSourceCollector)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.leadStatusState.collect(::leadStatusCollector)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.leadProbabilityState.collect(::leadProbabilityCollector)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.createLeadState.collect(::createLeadCollector)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.buttonSubmitState.collectLatest {
                    binding.btnSubmit.setEnable(requireContext(), it)
                }
            }
        }
    }

    private fun createLeadCollector(data: Resource<Boolean>) {
        with(binding) {
            data.collect(
                onLoading = {
                    pbCreateLead.makeVisible()
                },
                onSuccess = {
                    pbCreateLead.makeGone()
                    it?.let {
                        if (it) {
                            viewModel.setToDefaultState()
                            SuccessDialogFragment{
                                findNavController().popBackStack(R.id.createUpdateLeadFragment, true)
                            }.show(childFragmentManager, SuccessDialogFragment.TAG)
                        }
                    }
                },
                onError = { s: String?, i: Int ->
                    pbCreateLead.makeGone()
                    onNetworkEvent(i, s)
                }
            )
        }
    }

    private fun leadTypeCollector(data: Resource<HashMap<String, Int>>) {
        with(binding) {
            dropdownItemCollector(data, tilLeadType) {
                setDropdownAdapter(data.data, ddLeadType)
                if (viewModel.selectedDropdownItem.value.leadType.isNotEmpty()) {
                    ddLeadType
                        .setText(viewModel.selectedDropdownItem.value.leadType, false)
                }
            }
        }
    }

    private fun leadChannelCollector(data: Resource<HashMap<String, Int>>) {
        with(binding) {
            dropdownItemCollector(data, tilLeadChannel) {
                setDropdownAdapter(data.data, ddLeadChannel)
                if (viewModel.selectedDropdownItem.value.leadChannel.isNotEmpty()) {
                    ddLeadChannel
                        .setText(viewModel.selectedDropdownItem.value.leadChannel, false)
                }
            }
        }
    }

    private fun leadMediaCollector(data: Resource<HashMap<String, Int>>) {
        with(binding) {
            dropdownItemCollector(data, tilLeadMedia) {
                setDropdownAdapter(data.data, ddLeadMedia)
                if (viewModel.selectedDropdownItem.value.leadMedia.isNotEmpty()) {
                    ddLeadMedia
                        .setText(viewModel.selectedDropdownItem.value.leadMedia, false)
                }
            }
        }
    }

    private fun leadSourceCollector(data: Resource<HashMap<String, Int>>) {
        with(binding) {
            dropdownItemCollector(data, tilLeadSource) {
                setDropdownAdapter(data.data, ddLeadSource)
                if (viewModel.selectedDropdownItem.value.leadSource.isNotEmpty()) {
                    ddLeadSource
                        .setText(viewModel.selectedDropdownItem.value.leadSource, false)
                }
            }
        }
    }

    private fun leadStatusCollector(data: Resource<HashMap<String, Int>>) {
        with(binding) {
            dropdownItemCollector(data, tilLeadStatus) {
                setDropdownAdapter(data.data, ddLeadStatus)
                if (viewModel.selectedDropdownItem.value.leadStatus.isNotEmpty()) {
                    ddLeadStatus
                        .setText(viewModel.selectedDropdownItem.value.leadStatus, false)
                }
            }
        }
    }

    private fun leadProbabilityCollector(data: Resource<HashMap<String, Int>>) {
        with(binding) {
            dropdownItemCollector(data, tilLeadProbability) {
                setDropdownAdapter(data.data, ddLeadProbability)
                if (viewModel.selectedDropdownItem.value.leadProbability.isNotEmpty()) {
                    ddLeadProbability
                        .setText(viewModel.selectedDropdownItem.value.leadProbability, false)
                }
            }
        }
    }

    private fun <T> dropdownItemCollector(
        data: Resource<T>,
        textInputLayout: TextInputLayout,
        onSuccess: () -> Unit
    ) {
        data.collect(
            onLoading = { textInputLayout.helperText = "Loading..." },
            onSuccess = {
                textInputLayout.helperText = null
                onSuccess.invoke()
            },
            onError = { message: String?, codeError: Int ->
                textInputLayout.helperText = null
                onNetworkEvent(codeError, message)
            }
        )
    }

    private fun setDropdownAdapter(
        data: HashMap<String, Int>?,
        dropdownItem: AutoCompleteTextView
    ) {
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            data?.keys?.toList() ?: emptyList()
        )
        dropdownItem.setAdapter(arrayAdapter)
    }

    private fun setupTextChangeListener() {
        with(binding) {
            edtNotes.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatch(OnNotesChanged(text.toString()))
            }
            ddLeadType.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatch(OnLeadTypeChanged(text.toString()))
            }
            ddLeadChannel.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatch(OnLeadChannelChanged(text.toString()))
            }
            ddLeadMedia.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatch(OnLeadMediaChanged(text.toString()))
            }
            ddLeadSource.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatch(OnLeadSourceChanged(text.toString()))
            }
            ddLeadStatus.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatch(OnLeadStatusChanged(text.toString()))
            }
            ddLeadProbability.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatch(OnLeadProbabilityChanged(text.toString()))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}