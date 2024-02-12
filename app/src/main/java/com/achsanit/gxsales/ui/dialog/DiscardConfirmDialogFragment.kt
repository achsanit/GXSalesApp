package com.achsanit.gxsales.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.achsanit.gxsales.R
import com.achsanit.gxsales.databinding.FragmentDiscardConfirmDialogBinding

class DiscardConfirmDialogFragment(
    private val onOkayClick: () -> Unit
) : DialogFragment() {
    private var _binding: FragmentDiscardConfirmDialogBinding? = null
    private val binding get() =  _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_round_corner_dialog)
        _binding = FragmentDiscardConfirmDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnOkay.setOnClickListener {
                onOkayClick.invoke()
                dismiss()
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.75).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "discard.dialog.fragment"
    }
}