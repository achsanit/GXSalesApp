package com.achsanit.gxsales.ui.dialog

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.achsanit.gxsales.BuildConfig
import com.achsanit.gxsales.R
import com.achsanit.gxsales.databinding.FragmentPickImageDialogBinding
import kotlinx.coroutines.launch
import java.io.File

class PickImageDialogFragment(private val imagePickListener: (Uri) -> Unit) : DialogFragment() {
    private var _binding: FragmentPickImageDialogBinding? = null
    private val binding get() = _binding!!
    private var latestTmpUri: Uri? = null
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    imagePickListener(uri)
                    dismiss()
                }
            }
        }
    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imagePickListener(it)
                dismiss()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_round_corner_dialog)
        _binding = FragmentPickImageDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.75).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            cvGallery.setOnClickListener {
                selectImageFromGalleryResult.launch("image/*")
            }
            cvCamera.setOnClickListener {
                takeImage()
            }
        }
    }

    private fun takeImage() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getTmpFileUri().let {
                    latestTmpUri = it
                    takeImageResult.launch(it)
                }
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("Gx_Sales_", ".png")
            .apply {
                createNewFile()
                deleteOnExit()
            }
        return FileProvider.getUriForFile(
            requireContext().applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "pick.image.dialog"
    }

}