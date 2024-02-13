package com.achsanit.gxsales.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.achsanit.gxsales.R
import com.achsanit.gxsales.databinding.FragmentSettingLeadDialogBinding

class SettingLeadDialogFragment(
    private val titleDialog: String? = null,
    private val messageTitle: String,
    private val firstOptionText: String? = null,
    private val secondOptionText: String? = null,
    private val onFirstOptionClick: () -> Unit,
    private val onSecondOptionClick: () -> Unit
) : DialogFragment() {

    private var _binding: FragmentSettingLeadDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_round_corner_dialog)
        _binding = FragmentSettingLeadDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            titleDialog?.let {
                tvTitleDialog.text = it
            }
            tvMessageTitle.text = messageTitle
            firstOptionText?.let {
                tvOption1.text = it
            }
            secondOptionText?.let {
                tvOption2.text = it
            }
            tvOption1.setOnClickListener {
                onFirstOptionClick.invoke()
                dismiss()
            }
            tvOption2.setOnClickListener {
                onSecondOptionClick.invoke()
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.75).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "setting.lead.dialog"
    }
}