package com.example.tasky.ui.fragments.onBoarding

import android.app.Dialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tasky.MainViewModel
import com.example.tasky.R
import com.example.tasky.databinding.FragmentLogInBinding
import com.example.tasky.util.Constants.Companion.getProgressDialog
import com.example.tasky.util.Constants.Companion.makeLink
import com.example.tasky.util.ExtensionFunctionsConstants.emailAddTextChange
import com.example.tasky.util.NetworkResult
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()
//    private val viewModel by viewModels<OnBoardingViewModel>()

    private lateinit var progressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLogInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = getProgressDialog(requireContext())
        spanText()

        userDetailsValidation()


        loginOnClickListener()

        lifecycleScope.launch {
            viewModel.loginAuthResults.collect { result ->
                Log.d("login fragment", "${result.data} and $result")
                when(result) {
                    is NetworkResult.Loading -> {
                        progressDialog.show()
                    }
                    is NetworkResult.Success -> {
                        progressDialog.dismiss()
                        findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment())
                    }
                    is NetworkResult.Error -> {
                        progressDialog.dismiss()
                        Snackbar.make(binding.root, result.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
//        lifecycleScope.launch {
//            viewModel.loginResponse.observe(viewLifecycleOwner) { result ->
//                Log.d("login fragment", "${result.data} and ${result}")
//                when(result) {
//                    is NetworkResult.Success -> {
//                        progressDialog.dismiss()
//                        findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment())
//                    }
//                    is NetworkResult.Error -> {
//                        progressDialog.dismiss()
//                        Snackbar.make(binding.root, result.message.toString(), Snackbar.LENGTH_LONG).show()
//                    }
//                    is NetworkResult.Loading -> {
//                        progressDialog.show()
//                    }
//                }
//            }
//        }

    }

    private fun loginOnClickListener() {
        binding.loginButton.setOnClickListener {
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

            Log.d("login valid", binding.emailEditText.text.toString())
            viewModel.signIn(
                binding.emailEditText.text.toString(), binding.passwordEditText.text.toString()
            )

//            viewModel.login(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())


        }
    }

    private fun userDetailsValidation() {
        binding.emailEditText.emailAddTextChange()
    }

    private fun spanText() {

        val spannableString = makeLink(
            resources.getString(R.string.don_t_have_an_account_sign_up),
            "Sign up",
            R.color.blue
        ) {
            findNavController().navigate(R.id.signUpFragment)
        }

        binding.signUpText.movementMethod = LinkMovementMethod.getInstance()
        binding.signUpText.setText(spannableString, TextView.BufferType.SPANNABLE)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}