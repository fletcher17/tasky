package com.example.tasky.ui.fragments.onBoarding

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tasky.MainViewModel
import com.example.tasky.R
import com.example.tasky.auth.AuthResult
import com.example.tasky.databinding.FragmentLogInBinding
import com.example.tasky.util.Constants.Companion.getProgressDialog
import com.example.tasky.util.Constants.Companion.makeLink
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

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

        validation()


        login()

        lifecycleScope.launch {
            viewModel.loginAuthResults.collect { result ->
                Log.d("login fragment", "${result.data} and $result")
                when(result) {
                    is AuthResult.Loading -> {
                        progressDialog.show()
                    }
                    is AuthResult.Authorized -> {
                        progressDialog.dismiss()
                        findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment())
                    }
                    is AuthResult.Unauthorized -> {
                        progressDialog.dismiss()
                        Snackbar.make(binding.root, result.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
                    is AuthResult.UnKnownError -> {
                        progressDialog.dismiss()
                        Snackbar.make(binding.root, result.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

    private fun login() {
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

            viewModel.signIn(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )

        }
    }

    private fun validation() {
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

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