package com.achsanit.gxsales.ui.features.lead.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.achsanit.gxsales.R
import com.achsanit.gxsales.data.local.entity.CreateLeadData
import com.achsanit.gxsales.data.local.entity.DetailLeadEntity
import com.achsanit.gxsales.ui.features.lead.add.AddLeadViewModel.UIEvent.*
import com.achsanit.gxsales.databinding.FragmentFirstAddLeadBinding
import com.achsanit.gxsales.ui.dialog.DiscardConfirmDialogFragment
import com.achsanit.gxsales.ui.dialog.PickImageDialogFragment
import com.achsanit.gxsales.ui.features.lead.add.maps.MapsFragment
import com.achsanit.gxsales.ui.features.main.MainActivity
import com.achsanit.gxsales.utils.FileHelper
import com.achsanit.gxsales.utils.Resource
import com.achsanit.gxsales.utils.getAddress
import com.achsanit.gxsales.utils.getSizeKb
import com.achsanit.gxsales.utils.makeGone
import com.achsanit.gxsales.utils.makeInvisible
import com.achsanit.gxsales.utils.makeVisible
import com.achsanit.gxsales.utils.onNetworkEvent
import com.achsanit.gxsales.utils.setEnable
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class AddLeadFirstFragment : Fragment() {
    private var _binding: FragmentFirstAddLeadBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddLeadViewModel by activityViewModel()
    private val navArgs: AddLeadFirstFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstAddLeadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            DiscardConfirmDialogFragment {
                findNavController().navigateUp()
            }.show(childFragmentManager, DiscardConfirmDialogFragment.TAG)
        }

        if (navArgs.idLead > 0) {
            viewModel.dispatch(OnGetDetailLead(navArgs.idLead))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTextInputListener()
        setUpListener()
        setUpView()
        onCallBackSelectLocation()
    }

    private fun setUpView() {
        with(binding) {
            btnNext.setOnClickListener {
                val action = AddLeadFirstFragmentDirections
                    .actionCreateUpdateLeadFragmentToAddLeadSecondFragment()
                    .setIdLead(navArgs.idLead)
                findNavController().navigate(action)
            }

            cvAddIdentityImage.setOnClickListener { openImagePicker() }

            ivDeleteSelectedImage.setOnClickListener {
                viewModel.dispatch(OnImageIDNumberChanged(null))
                cvAddIdentityImage.makeVisible()
                cvSelectedIdentityImage.makeGone()
            }

            ivEditSelectedImage.setOnClickListener {
                openImagePicker()
            }

            ivViewSelectedImage.setOnClickListener {
                viewModel.mainData.value.idNumberPhoto?.let {
                    val action = AddLeadFirstFragmentDirections
                        .actionCreateUpdateLeadFragmentToImageViewerFragment(it.toUri().toString())
                    findNavController().navigate(action)
                }
            }

            ibBackToolbar.setOnClickListener {
                DiscardConfirmDialogFragment {
                    findNavController().navigateUp()
                }.show(childFragmentManager, DiscardConfirmDialogFragment.TAG)
            }

            btnSelectLocation.setOnClickListener { openMaps() }

            rbGroup.setOnCheckedChangeListener { _, i ->
                val gender = rbGroup.findViewById<RadioButton>(i).text
                viewModel.dispatch(OnGenderChanged(gender.toString()))
            }

            val prefixPhones = resources.getStringArray(R.array.list_prefix_phone).toList()
            val prefixAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, prefixPhones)
            ddPrefixPhone.setAdapter(prefixAdapter)
            if (viewModel.selectedDropdownItem.value.prefixPhone.isNotEmpty()) {
                ddPrefixPhone.setText(viewModel.selectedDropdownItem.value.prefixPhone, false)
            }

            with(viewModel.mainData.value) {
                edtFullName.setText(fullName)
                edtEmail.setText(email)
                edtCellPhone.setText(phone)

                if (address.isNotEmpty()) {
                    edtAddress.setText(address)
                } else if (latitude.isNotEmpty()) {
                    edtAddress.setText(getAddress(latitude.toDouble(), longitude.toDouble()))
                }

                edtIdentityNumber.setText(idNumber)

                if (gender.isNotEmpty()) {
                    rbGroup.check(
                        if (gender == "Male" || gender == "male")
                            R.id.rb_male else R.id.rb_female
                    )
                }
            }
        }
    }

    private fun setUpTextInputListener() {
        with(binding) {
            edtFullName.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatch(OnFullNameChanged(text.toString()))
            }
            edtEmail.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatch(OnEmailChanged(text.toString()))
            }
            edtCellPhone.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatch(OnPhoneChanged(text.toString()))
            }
            edtAddress.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatch(OnAddressChanged(text.toString()))
            }
            edtIdentityNumber.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatch(OnIdentityNumberChanged(text.toString()))
            }
            dropdownBranch.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatch(OnBranchOfficeChanged(text.toString()))
            }
            ddPrefixPhone.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatch(OnPrefixPhoneChanged(text.toString()))
            }
        }
    }

    private fun setUpListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.mainData.collect(::setDataWhenChanged)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.getBranchState.collect(::branchStateCollector)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.buttonNextState.collect {
                    binding.btnNext.setEnable(requireContext(), it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.detailLeadState.collect(::detailLeadStateCollector)
            }
        }
    }

    private fun detailLeadStateCollector(data: Resource<DetailLeadEntity>) {
        with(binding) {
            data.collect(
                onLoading = {
                    pbDetail.makeVisible()
                },
                onSuccess = {
                    pbDetail.makeGone()
                    it?.let { data ->
                        viewModel.updateDetailData(data)

                        dropdownBranch.setText(data.branchOfficeName, false)
                        edtFullName.setText(data.fullName)
                        edtEmail.setText(data.email)
                        edtCellPhone.setText(data.phone)
                        edtLatitude.setText(data.latitude)
                        edtLongitude.setText(data.longitude)
                        edtAddress.setText(data.address)
                        edtIdentityNumber.setText(data.idNumber)
                        if (data.gender.isNotEmpty()) {
                            rbGroup.check(
                                if (data.gender == "Male" || data.gender == "male")
                                    R.id.rb_male else R.id.rb_female
                            )
                        }
                    }
                    Toast.makeText(requireContext(), "Get detail data success", Toast.LENGTH_SHORT)
                        .show()
                },
                onError = { s: String?, i: Int ->
                    pbDetail.makeGone()
                    onNetworkEvent(i, s)
                }
            )
        }
    }

    private fun branchStateCollector(data: Resource<HashMap<String, Int>>) {
        with(binding) {
            when (data) {
                is Resource.Loading -> {
                    tilBranchOffice.helperText = "Loading..."
                }

                is Resource.Success -> {
                    tilBranchOffice.helperText = null
                    val listBranch = data.data?.keys?.toList() ?: emptyList()

                    val arrayAdapter =
                        ArrayAdapter(requireContext(), R.layout.item_spinner, listBranch)
                    dropdownBranch.setAdapter(arrayAdapter)

                    if (viewModel.selectedDropdownItem.value.branchOffice.isNotEmpty()) {
                        dropdownBranch
                            .setText(viewModel.selectedDropdownItem.value.branchOffice, false)
                    }
                }

                else -> {
                    tilBranchOffice.helperText = null
                    onNetworkEvent(data.codeError, "error get list of branch office")
                }
            }
        }
    }

    private fun setDataWhenChanged(data: CreateLeadData) {
        with(binding) {
            edtLatitude.setText(data.latitude)
            edtLongitude.setText(data.longitude)

            data.idNumberPhoto?.let {
                cvSelectedIdentityImage.makeVisible()
                cvAddIdentityImage.makeInvisible()

                tvFileName.text = it.name
                tvFileSize.text =
                    resources.getString(
                        R.string.text_file_size_in_kb_placeholder,
                        it.getSizeKb().toString()
                    )
            } ?: {
                cvAddIdentityImage.makeVisible()
                cvSelectedIdentityImage.makeGone()
            }
        }
    }

    private fun onCallBackSelectLocation() {
        val stateHandle = findNavController().currentBackStackEntry?.savedStateHandle
        stateHandle?.getLiveData<LatLng>(MapsFragment.SELECTED_LOCATION_KEY)?.let { latLng ->
            latLng.observe(viewLifecycleOwner) {
                val selectedAddress = getAddress(it.latitude, it.longitude)
                viewModel.dispatch(
                    OnLocationChanged(
                        address = selectedAddress,
                        latitude = it.latitude.toString(),
                        longitude = it.longitude.toString()
                    )
                )
                binding.edtAddress.setText(selectedAddress)
            }
        }
        stateHandle?.remove<LatLng>(MapsFragment.SELECTED_LOCATION_KEY)
    }

    private fun openMaps() {
        if ((activity as MainActivity).hasLocationPermission()) {
            val action = AddLeadFirstFragmentDirections
                .actionCreateUpdateLeadFragmentToMapsFragment()
            findNavController().navigate(action)
        } else {
            (activity as MainActivity).requestLocationPermission()
        }
    }

    private fun openImagePicker() {
        if ((activity as MainActivity).hasCameraStoragePermission()) {
            PickImageDialogFragment {
                lifecycleScope.launch {
                    val file = FileHelper.createTmpFileFromUri(
                        requireContext(),
                        it,
                        "Gx_Sales_"
                    )

                    withContext(Dispatchers.Main) {
                        file?.let { viewModel.dispatch(OnImageIDNumberChanged(it)) }
                    }
                }
            }.show(childFragmentManager, PickImageDialogFragment.TAG)
        } else {
            (activity as MainActivity).requestCameraStoragePermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        setUpView()
        onCallBackSelectLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setToDefaultState()
    }
}