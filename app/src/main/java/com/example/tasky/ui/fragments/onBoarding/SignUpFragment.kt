package com.example.tasky.ui.fragments.onBoarding

import android.app.Dialog
import android.net.Network
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tasky.MainViewModel
import com.example.tasky.R
import com.example.tasky.auth.AuthResult
import com.example.tasky.auth.AuthSignUpRequest
import com.example.tasky.databinding.FragmentSignUpBinding
import com.example.tasky.util.Constants.Companion.getProgressDialog
import com.example.tasky.util.ExtensionFunctionsConstants.emailAddTextChange
import com.example.tasky.util.ExtensionFunctionsConstants.nameAddTextChange
import com.example.tasky.util.NetworkResult
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()
//    private val viewModel by viewModels<OnBoardingViewModel>()

    private lateinit var processDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        processDialog = getProgressDialog(requireContext())

        binding.navigateButton.setOnClickListener {
            val isPop = findNavController().popBackStack()
            Log.d("backstack", "$isPop")
            if (!isPop) {
                Log.d("backstack false", "$isPop")
                requireActivity().finish()
            } else {
                Log.d("backstack true", "$isPop")
                findNavController().navigateUp()
            }
        }



        signUpOnClickListener()

        binding.nameEditText.nameAddTextChange()

        binding.emailEditText.emailAddTextChange()

        lifecycleScope.launch {
            viewModel.authResults.collect { result ->
                when(result) {
                    is NetworkResult.Success -> {
                        processDialog.dismiss()
                        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment())
                    }
                    is NetworkResult.Error -> {
                        processDialog.dismiss()
                        Snackbar.make(binding.root, result.message.toString(), Snackbar.LENGTH_LONG)
                            .show()
                    }
                    is NetworkResult.Loading -> {
                        processDialog.show()
                    }
                }
            }
        }
        return binding.root
    }

    private fun signUpOnClickListener() {
        binding.getStartedButton.setOnClickListener {
            if (binding.nameEditText.text.isNullOrEmpty()) {
                binding.nameEditText.apply {
                    requestFocus()
                    error = "Name must not be empty"
                }
                return@setOnClickListener
            }

            if (binding.emailEditText.text.isNullOrEmpty()) {
                binding.emailEditText.apply {
                    requestFocus()
                    error = "Email Address must not be empty"
                }
                return@setOnClickListener
            }
            if (binding.passwordEditText.text.isNullOrEmpty()) {
                binding.passwordEditText.apply {
                    requestFocus()
                    error = "Enter password"
                }
                return@setOnClickListener
            }

            viewModel.signUp(
                    binding.nameEditText.text.toString(),
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
            )

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}