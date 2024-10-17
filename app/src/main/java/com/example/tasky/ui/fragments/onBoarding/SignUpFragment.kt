package com.example.tasky.ui.fragments.onBoarding

import android.app.Dialog
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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

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



        signUp()

        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.isNotEmpty() == true) {
                    binding.nameEditText.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_check,
                            resources.newTheme()
                        ),
                        null
                    )
                } else {
                    binding.nameEditText.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        null,
                        null
                    )
                }
            }

        })

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (Patterns.EMAIL_ADDRESS.matcher(p0.toString()).matches()) {
                    binding.emailEditText.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_check,
                            resources.newTheme()
                        ),
                        null
                    )
                } else {
                    binding.emailEditText.error = "Email format is wrong"
                    binding.emailEditText.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        null,
                        null
                    )
                }
            }

        })

        lifecycleScope.launch {
            viewModel.authResults.collect { result ->
                when (result) {
                    is AuthResult.Authorized -> {
                        processDialog.dismiss()
                        findNavController().navigate(R.id.homeFragment)
                    }

                    is AuthResult.Loading -> {
                        processDialog.show()
                    }

                    is AuthResult.Unauthorized -> {
                        processDialog.dismiss()
                        Snackbar.make(binding.root, result.message.toString(), Snackbar.LENGTH_LONG).show()

                    }

                    is AuthResult.UnKnownError -> {
                        processDialog.dismiss()
                        Snackbar.make(binding.root, result.message.toString(), Snackbar.LENGTH_LONG).show()

                    }
                }
            }
        }




        return binding.root
    }

    private fun signUp() {
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