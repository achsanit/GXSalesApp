package com.achsanit.gxsales.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.achsanit.gxsales.R
import com.achsanit.gxsales.data.local.DataStorePreference
import com.achsanit.gxsales.data.local.SharedPreferencesManager
import com.achsanit.gxsales.databinding.FragmentSessionEndedDialogBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SessionEndedDialogFragment(
    private val navigation: () -> Unit
) : DialogFragment() {

    private var _binding: FragmentSessionEndedDialogBinding? = null
    private val binding get() = _binding!!

    private val dataPref by inject<DataStorePreference>()
    private val sharedPref by inject<SharedPreferencesManager>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_round_corner_dialog)
        _binding = FragmentSessionEndedDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnOkay.setOnClickListener {
                lifecycleScope.launch {
                    dataPref.setLogoutData()
                    sharedPref.saveData(SharedPreferencesManager.TOKEN_KEY, "")
                }
                dismiss()
                navigation.invoke()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.75).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.isCancelable = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "session.ended.dialog"
    }
}