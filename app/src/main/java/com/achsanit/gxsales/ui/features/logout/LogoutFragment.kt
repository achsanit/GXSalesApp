package com.achsanit.gxsales.ui.features.logout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.achsanit.gxsales.BuildConfig
import com.achsanit.gxsales.R
import com.achsanit.gxsales.data.local.entity.ProfileEntity
import com.achsanit.gxsales.databinding.FragmentLogoutBinding
import com.achsanit.gxsales.ui.features.login.LoginActivity
import com.achsanit.gxsales.utils.Resource
import com.achsanit.gxsales.utils.makeGone
import com.achsanit.gxsales.utils.makeVisible
import com.achsanit.gxsales.utils.onNetworkEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LogoutFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentLogoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LogoutViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        with(binding) {
            textAppVersion.text =
                resources.getString(R.string.text_version_placeholder, BuildConfig.VERSION_NAME)

            btnLogout.setOnClickListener {
                viewModel.logout()
            }
        }
    }

    private fun setupListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                viewModel.profileState.collect(::profileStateCollector)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                viewModel.logoutState.collect(::logoutStateCollector)
            }
        }
    }

    private fun logoutStateCollector(data: Resource<Boolean>) {
        with(binding) {
            when(data) {
                is Resource.Loading -> {
                    pbLogout.makeVisible()
                }
                is Resource.Success -> {
                    pbLogout.makeGone()
                    data.data?.let {
                        if (it) {
                            dismiss()
                            val intent = Intent(requireActivity(), LoginActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    }
                }
                else -> {
                    pbLogout.makeGone()
                    onNetworkEvent(data.codeError)
                }
            }
        }
    }

    private fun profileStateCollector(data: Resource<ProfileEntity>) {
        with(binding) {
            when (data) {
                is Resource.Loading -> {
                    pbLogout.makeVisible()
                }

                is Resource.Success -> {
                    pbLogout.makeGone()
                    tvUserName.text = data.data?.name.toString()
                    tvUserEmail.text = data.data?.email.toString()
                }

                else -> {
                    pbLogout.makeGone()
                    onNetworkEvent(data.codeError)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}